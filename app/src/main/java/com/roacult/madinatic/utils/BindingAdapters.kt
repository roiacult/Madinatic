package com.roacult.madinatic.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.roacult.madinatic.R
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("showInt")
fun TextView.showInt(int :Int){
    text = int.toString()
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(url:String?){
    if(url != null && url.isNotEmpty() ) Picasso.get().load(url).into(this)
}

@BindingAdapter("loadUri")
fun ImageView.loadImageUri(path : String) {

    if (path.endsWith("pdf")) {
        setImageResource(R.drawable.ic_doc2)
    }else if(path.endsWith("doc") || path.endsWith("docx")){
        setImageResource(R.drawable.ic_pdf)
    }
    else if(path.endsWith("jpg") || path.endsWith("png") ||path.endsWith("gif")) {
        setImageURI(Uri.fromFile(File(path)))
    }else {
        setImageResource(R.drawable.ic_document)
    }
}