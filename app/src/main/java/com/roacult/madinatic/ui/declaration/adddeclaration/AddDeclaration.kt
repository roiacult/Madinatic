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
import co.lujun.androidtagview.TagView
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AddDeclarationBinding
import timber.log.Timber
import java.util.regex.Pattern

class AddDeclaration : FullScreenFragment<AddDeclarationBinding>() {
    override val layutIdRes = R.layout.add_declaration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        //TODO remove this later (this just for testing the ui)
        val list = ArrayList<String>().apply {
            add("categorie of declaration")
            add("categorie test1")
            add("categorie test2")
        }
        binding.spinner.adapter = SpinnerAdapter(context!!,android.R.layout.simple_dropdown_item_1line,list)
        binding.addfile.setOnClickListener {
            checkPermission()
        }
        binding.tagView.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onSelectedTagDrag(position: Int, text: String?) {}
            override fun onTagLongClick(position: Int, text: String?) {}
            override fun onTagClick(position: Int, text: String?) {}
            override fun onTagCrossClick(position: Int) {
                binding.tagView.removeTag(position)
            }
        })
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
            withSupportFragment(this@AddDeclaration)
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
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Timber.v("pickfile data ... ${data?.data}")
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            Timber.v("file path ... $filePath")
            if(filePath != null) {
                val paths = filePath.split("/")
                binding.tagView.addTag(paths[paths.size -1])
            }
        }
    }

    companion object {
        const val PICKFILE_REQUEST_CODE = 984
        const val REQUEST_STORAGE_PERMISSION = 875
    }
}