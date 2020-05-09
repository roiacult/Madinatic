package com.roacult.madinatic.ui.declaration.adddeclaration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AddDeclarationBinding
import timber.log.Timber

class AddDeclaration : FullScreenFragment<AddDeclarationBinding>() {
    override val layutIdRes = R.layout.add_declaration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val list = ArrayList<String>().apply {
            add("categorie of declaration")
            add("categorie test1")
            add("categorie test2")
        }
        binding.spinner.adapter = SpinnerAdapter<String>(context!!,android.R.layout.simple_dropdown_item_1line,list)
        binding.addfile.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/pdf"
                addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(Intent.createChooser(this , "Select Picture"), PICKFILE_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Timber.v("pickfile data ... ${data?.data}")
            val paths = data?.data?.pathSegments
            Timber.v("pickfile path ... $paths")
            if(paths != null){
                val path = paths[paths.size-1]
                binding.tagView.addTag(path)
            }
        }
    }

    companion object {
        const val PICKFILE_REQUEST_CODE = 984
    }
}