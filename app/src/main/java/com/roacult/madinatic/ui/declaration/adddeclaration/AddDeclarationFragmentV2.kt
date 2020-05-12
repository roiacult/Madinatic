package com.roacult.madinatic.ui.declaration.adddeclaration

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AddDeclarationV2Binding
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Success
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.regex.Pattern

class AddDeclarationFragmentV2 : FullScreenFragment<AddDeclarationV2Binding>() {

    override val layutIdRes = R.layout.add_declaration_v2
    private val viewModel : AddDeclarationViewModel by viewModel()
    private val controller by lazy {
        DeclarationDocController(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.observe(this){
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            it.addDocClickEvent?.getContentIfNotHandled()?.let(::addDocClick)
            it.declarationDoc.let(controller::setData)
            handleCategories(it.categories)
//            handleSubmition(it.addDeclaration)
        }
    }

    private fun addDocClick(none: None) {
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
            withSupportFragment(this@AddDeclarationFragmentV2)
            withCloseMenu(true)
            withRequestCode(AddDeclarationFragment.PICKFILE_REQUEST_CODE)
            withFilter(Pattern.compile(".*\\.(pdf|docx|doc|txt|jpg|png|gif)$"))
            start()
        }
    }

    private fun handleCategories(categories: Async<List<CategorieView>>) {
        if(categories is Success) {
            val cat = categories()
            if(cat.size == binding.type.adapter?.count) return
            binding.type.adapter = SpinnerAdapter(context!!,android.R.layout.simple_dropdown_item_1line,categories())
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

        binding.save.setOnClickListener {
            viewModel.save(
                binding.title.text.toString(),
                binding.desciption.text.toString(),
                binding.type.selectedItem as? CategorieView
            )
        }

        binding.materialButton.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withGeolocApiKey(getString(R.string.google_api_key))
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withSatelliteViewHidden()
                .withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .build(context!!)
            startActivityForResult(locationPickerIntent, MAP_REQUEST_CODE)
        }

        binding.cancel.setOnClickListener {
            activity?.onBackPressed()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Timber.v("pickfile data ... ${data?.data}")
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            Timber.v("file path ... $filePath")
            if(filePath != null) {
                viewModel.addDoc(filePath)
            }

        }else if(requestCode == MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val latitude = data.getDoubleExtra(LATITUDE, 0.0)
            val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
            val address = data.getStringExtra(LOCATION_ADDRESS)

            if(address != null)  binding.materialButton.text = address


            viewModel.adrress = Address(address ?: "",latitude,longitude)
        }
    }

    companion object {
        const val PICKFILE_REQUEST_CODE = 984
        const val REQUEST_STORAGE_PERMISSION = 875
        const val MAP_REQUEST_CODE =958
    }
}