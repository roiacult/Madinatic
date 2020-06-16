package com.roacult.madinatic.ui.profile.unfinished.update_declaration

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AddDeclarationV2Binding
import com.roacult.madinatic.ui.declaration.adddeclaration.CategorieView
import com.roacult.madinatic.ui.declaration.adddeclaration.DeclarationDocController
import com.roacult.madinatic.ui.declaration.adddeclaration.HINT_VIEW_ID
import com.roacult.madinatic.ui.declaration.adddeclaration.SpinnerAdapter
import com.roacult.madinatic.utils.states.*
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.regex.Pattern

class UpdateDeclarationFragment : FullScreenFragment<AddDeclarationV2Binding>() {
    override val layutIdRes = R.layout.add_declaration_v2

    private val viewModel: UpdateDeclarationViewModel by viewModel()
    private val controller by lazy {
        DeclarationDocController(viewModel)
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity!!)
    }

    private val locationRequest by lazy{
        LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
    private val locationBuilder by lazy{
        LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
        }
    }

    private var firstTime = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.observe(this){
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            it.addDocClickEvent?.getContentIfNotHandled()?.let(::addDocClick)
            it.declarationDoc.let(::addDocs)
            handleCategories(it.categories)
            handleSubmition(it.addDeclaration)
        }
    }

    private fun addDocs(docs: List<String>) {
        val list = docs.toMutableList()
        list.addAll(viewModel.declaration.attachment.map { it.src })
        controller.setData(list)
    }

    private fun handleSubmition(addDeclaration: Async<OperationType>) {
        when(addDeclaration){
            is Loading -> binding.state = ViewState.LOADING
            is Fail<*, *> -> binding.state = ViewState.EMPTY
            is Success -> {
                binding.state = ViewState.EMPTY
                if(addDeclaration() == OperationType.UPDATING)
                    successDialogue()
                else {
                    showMessage("declaration deleted successfully")
                    activity?.onBackPressed()
                    vm.refresh()
                }
            }
        }
    }

    private fun successDialogue() {
        AlertDialog.Builder(context!!).apply {
            setTitle(R.string.sub_title)
            setMessage(R.string.sub_msg)
            setPositiveButton(R.string.close){_,_-> }
            setOnDismissListener {
                activity?.onBackPressed()
            }
            show()
        }
    }

    private fun addDocClick(none: None) {

        AlertDialog.Builder(context!!).apply {
            setItems(R.array.doc_options){ _, selected ->
                when (selected) {
                    0 -> {
                        CropImage.activity()
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .start(context, this@UpdateDeclarationFragment)
                    }
                    1 -> {
                        addFile()
                    }
                }
            }
            show()
        }

    }

    private fun addFile() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(context!!).apply {
                    setTitle(R.string.permi_titel)
                    setMessage(R.string.permi_msg)
                    setPositiveButton(R.string.close){_,_->
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_STORAGE_PERMISSION
                        )
                    }
                    show()
                }
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_STORAGE_PERMISSION
                )
            }
        } else showFilePicker()
    }

    private fun showFilePicker() {
        MaterialFilePicker().apply {
            withSupportFragment(this@UpdateDeclarationFragment)
            withCloseMenu(true)
            withRequestCode(PICKFILE_REQUEST_CODE)
            withFilter(Pattern.compile(".*\\.(pdf|docx|doc|txt)$"))
            start()
        }
    }

    private fun handleCategories(categories: Async<List<CategorieView>>) {
        if(categories is Success) {
            val cat = categories()
            binding.type.adapter = SpinnerAdapter(
                context!!,android.R.layout.simple_dropdown_item_1line,
                cat
            )
            val index = cat.map { it.idc  }.indexOf(viewModel.categorie)
            if(index >= 0 && firstTime) {
                firstTime = false
                binding.type.setSelection(index)
            }
        }else {
            val hintCategorie = CategorieView(HINT_VIEW_ID,getString(R.string.type_dec),"")
            binding.type.adapter = SpinnerAdapter(
                context!!,
                android.R.layout.simple_dropdown_item_1line,
                arrayListOf(hintCategorie)
            )
        }
    }

    private fun initViews() {
        viewModel.saveData(arguments!!.getString(DECLARATION,""))
        syncData()

        binding.toolbar.title = getString(R.string.update_dec)
        val manager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.toolbar.inflateMenu(R.menu.update_declaration_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete_declaration-> {
                    showConfirmDeleteDialog()
                    true
                }
                else -> false
            }
        }

        binding.epoxyRecyclerView.layoutManager = manager
        binding.epoxyRecyclerView.setController(controller)
        binding.save.setOnClickListener {
            saveData()
            viewModel.save()
        }

        binding.toolbar.setNavigationIcon(R.drawable.back)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.materialButton.setOnClickListener(::checkLocationSettings)

        binding.cancel.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(context!!).apply {
            setTitle(R.string.delete_dec_title)
            setMessage(R.string.delete_dec_msg)
            setPositiveButton(R.string.delete){_,_->
                viewModel.deleteDeclaration()
            }
            setNegativeButton(R.string.cancel){_,_->}
            show()
        }
    }

    private fun syncData() {
        binding.title.setText(viewModel.title)
        binding.desciption.setText(viewModel.description)
        binding.materialButton.text = viewModel.address.name
    }

    private fun saveData(){
        viewModel.title = binding.title.text.toString()
        viewModel.description = binding.desciption.text.toString()
        viewModel.categorie = (binding.type.selectedItem as CategorieView).idc
    }

    private fun checkLocationSettings(v:View) {
        LocationServices.getSettingsClient(activity!!).checkLocationSettings(locationBuilder.build())
            .addOnCompleteListener {
                try{
                    it.getResult(ApiException::class.java)
                    requestLocationAndStartLocationPicker()
                } catch (exception: ApiException) {
                    when(exception.statusCode){
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{
                            val resolvable = (exception as ResolvableApiException)
                            startIntentSenderForResult(
                                resolvable.resolution.intentSender,
                                GPS_ACTIVATION_CODE,null, 0, 0, 0, null
                            )
                        }
                    }
                }
            }
    }

    fun requestLocationAndStartLocationPicker(){
        fusedLocationClient.requestLocationUpdates(locationRequest,
            object : LocationCallback(){},
            Looper.getMainLooper())
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withGooglePlacesApiKey(getString(R.string.google_map_api_key))
            .withDefaultLocaleSearchZone()
            .withGoogleTimeZoneEnabled()
            .build(context!!)
        startActivityForResult(locationPickerIntent, MAP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Timber.v("pickfile data ... ${data?.data}")
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            Timber.v("file path ... $filePath")
            if(filePath != null) {
                viewModel.addDoc(filePath)
            }
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                viewModel.addDoc(result.uri.path!!)
            }
        }else if(requestCode == MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val latitude = data.getDoubleExtra(LATITUDE, 0.0)
            val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
            val address = data.getStringExtra(LOCATION_ADDRESS)
            if(address != null)  binding.materialButton.text = address
            viewModel.address = Address(address ?: "",latitude,longitude)
        }else if (requestCode==GPS_ACTIVATION_CODE){
            if(resultCode == Activity.RESULT_OK) {
                requestLocationAndStartLocationPicker()
            }else {
                showMessage(R.string.location_gps_fail)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode ==REQUEST_STORAGE_PERMISSION)  {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showFilePicker()
            } else showMessage(R.string.upload_permission_denied)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        saveData()
    }

    companion object {

        const val PICKFILE_REQUEST_CODE = 984
        const val REQUEST_STORAGE_PERMISSION = 875
        const val MAP_REQUEST_CODE =958
        const val GPS_ACTIVATION_CODE = 123

        const val DECLARATION = "com.roacult.madinatic:declaration"
    }
}