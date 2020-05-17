package com.roacult.madinatic.ui.profile.unfinished.adddoc

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.regex.Pattern

class AddDocFragment : FullScreenFragment<AddDeclarationV2Binding>() {
    override val layutIdRes = R.layout.add_declaration_v2

    private val viewModel: AddDocViewModel by viewModel()
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
            handleSubmition(it.addDeclaration)
        }
    }

    private fun handleSubmition(addDeclaration: Async<None>) {
        when(addDeclaration){
            is Loading -> binding.state = ViewState.LOADING
            is Fail<*, *> -> binding.state = ViewState.EMPTY
            is Success -> {
                binding.state = ViewState.EMPTY
                successDialogue()
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
                            .start(context, this@AddDocFragment)
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
            withSupportFragment(this@AddDocFragment)
            withCloseMenu(true)
            withRequestCode(PICKFILE_REQUEST_CODE)
            withFilter(Pattern.compile(".*\\.(pdf|docx|doc|txt)$"))
            start()
        }
    }

    private fun handleCategories(categories: Async<List<CategorieView>>) {
        if(categories is Success) {
            val cat = categories().filter { it.idc == viewModel.declaration.categorie }
            binding.type.adapter = SpinnerAdapter(
                context!!,android.R.layout.simple_dropdown_item_1line,
                cat
            )
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
        binding.toolbar.title = getString(R.string.add_doc)
        // disable all views except add document view
        binding.title.isEnabled = false
        binding.title.setText(viewModel.declaration.title)
        binding.desciption.isEnabled = false
        binding.desciption.setText(viewModel.declaration.desc)
        binding.type.isEnabled = false
        binding.materialButton.isEnabled = false
        binding.materialButton.text = viewModel.declaration.address

        val manager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.epoxyRecyclerView.layoutManager = manager
        binding.epoxyRecyclerView.setController(controller)
        binding.save.setOnClickListener { viewModel.save() }

        binding.toolbar.setNavigationIcon(R.drawable.back)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.cancel.setOnClickListener {
            activity?.onBackPressed()
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
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                viewModel.addDoc(result.uri.path!!)
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

    companion object {

        const val PICKFILE_REQUEST_CODE = 984
        const val REQUEST_STORAGE_PERMISSION = 875

        const val DECLARATION = "com.roacult.madinatic:declaration"
    }
}