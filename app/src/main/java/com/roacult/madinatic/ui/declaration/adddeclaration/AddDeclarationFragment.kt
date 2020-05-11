package com.roacult.madinatic.ui.declaration.adddeclaration

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.lujun.androidtagview.TagView
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.roacult.domain.entities.Categorie
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AddDeclarationBinding
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Fail
import com.roacult.madinatic.utils.states.Loading
import com.roacult.madinatic.utils.states.Success
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import timber.log.Timber
import java.util.regex.Pattern
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddDeclarationFragment : FullScreenFragment<AddDeclarationBinding>(),View.OnClickListener {
    override val layutIdRes = R.layout.add_declaration

    private val viewModel : AddDeclarationViewModel by viewModel()
    private var selectedPhoto = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.observe(this){
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            handleCategories(it.categories)
        }
    }

    private fun handleCategories(categories: Async<List<CategorieView>>) {
        when(categories){
            is Loading -> {
                binding.spinner.isEnabled = false
            }
            is Success -> {
                binding.spinner.isEnabled = true
                binding.spinner.adapter = SpinnerAdapter(context!!,android.R.layout.simple_dropdown_item_1line,categories())
            }
        }
    }

    private fun initViews() {

        binding.addfile.setOnClickListener {
            checkPermission()
        }

        binding.tagView.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onSelectedTagDrag(position: Int, text: String?) {}
            override fun onTagLongClick(position: Int, text: String?) {}
            override fun onTagClick(position: Int, text: String?) {}
            override fun onTagCrossClick(position: Int) {
                binding.tagView.removeTag(position)
                viewModel.files.removeAt(position)
            }
        })

        binding.image1.setOnClickListener(this)
        binding.image2.setOnClickListener(this)
        binding.image3.setOnClickListener(this)
        binding.image4.setOnClickListener(this)
        binding.image5.setOnClickListener(this)
        binding.image6.setOnClickListener(this)

        binding.save.setOnClickListener {
            viewModel.save(
                binding.title.text.toString(),
                binding.desciption.text.toString(),
                binding.spinner.selectedItem as CategorieView
            )
        }

        binding.materialButton2.setOnClickListener {
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

    private fun loadDataFromViewModel() {

        //load images from viewModel
        if(viewModel.images[0].isNotEmpty()){
            binding.image1.setImageURI(Uri.fromFile(File(viewModel.images[0])))
        }
        if(viewModel.images[1].isNotEmpty()){
            binding.image2.setImageURI(Uri.fromFile(File(viewModel.images[1])))
        }
        if(viewModel.images[2].isNotEmpty()){
            binding.image3.setImageURI(Uri.fromFile(File(viewModel.images[2])))
        }
        if(viewModel.images[3].isNotEmpty()){
            binding.image4.setImageURI(Uri.fromFile(File(viewModel.images[3])))
        }
        if(viewModel.images[4].isNotEmpty()){
            binding.image5.setImageURI(Uri.fromFile(File(viewModel.images[4])))
        }
        if(viewModel.images[5].isNotEmpty()){
            binding.image6.setImageURI(Uri.fromFile(File(viewModel.images[5])))
        }

        //load files from viewModel
        for( filePath in viewModel.files){
            val paths = filePath.split("/")
            binding.tagView.addTag(paths[paths.size -1])
        }

        //load address
        if(viewModel.adrress != null) {
            binding.materialButton2.text = viewModel.adrress!!.name
        }
    }

    private fun checkPermission() {
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
                        request()
                    }
                    show()
                }
            } else {
                request()
            }
        } else {
            showFilePicker()
        }
    }

    private fun request() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_STORAGE_PERMISSION
        )
    }

    private fun showFilePicker(){
        MaterialFilePicker().apply {
            withSupportFragment(this@AddDeclarationFragment)
            withCloseMenu(true)
            withRequestCode(PICKFILE_REQUEST_CODE)
            withFilter(Pattern.compile(".*\\.(pdf|docx|doc|txt)$"))
            start()
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
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Timber.v("pickfile data ... ${data?.data}")
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            Timber.v("file path ... $filePath")
            if(filePath != null) {
                val paths = filePath.split("/")
                viewModel.files.add(filePath)
                binding.tagView.addTag(paths[paths.size -1])
            }
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                viewModel.images[getIndex(selectedPhoto)] = result.uri.path!!
                binding.root.findViewById<ImageView>(selectedPhoto).apply {
                    setImageURI(result.uri)
                }
            }
        }else if(requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val latitude = data.getDoubleExtra(LATITUDE, 0.0)
            val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
            val address = data.getStringExtra(LOCATION_ADDRESS)

            if(address != null) binding.materialButton2.text = address

            viewModel.adrress = Address(address ?: "",latitude,longitude)

            Timber.v("lat : $latitude")
            Timber.v("long : $longitude")
            Timber.v("address : $address")

        }
    }

    private fun getIndex(id: Int): Int {
        return when(id){
            R.id.image1-> return 0
            R.id.image2-> return 1
            R.id.image3-> return 2
            R.id.image4-> return 3
            R.id.image5-> return 4
            R.id.image6-> return 5
            else -> 0
        }
    }

    override fun onClick(v: View) {
        selectedPhoto = v.id
        Timber.v("onClick !!!!!!")
        CropImage.activity()
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(context!!, this)
    }

    companion object {
        const val PICKFILE_REQUEST_CODE = 984
        const val REQUEST_STORAGE_PERMISSION = 875
        const val MAP_REQUEST_CODE =958
    }
}