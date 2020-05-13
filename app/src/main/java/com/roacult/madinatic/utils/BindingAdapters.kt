package com.roacult.madinatic.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.roacult.domain.entities.Attachment
import com.roacult.domain.entities.AttachmentType
import com.roacult.domain.entities.DeclarationState
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
    setImageURI(Uri.fromFile(File(path)))
}

@BindingAdapter("loadicon")
fun ImageView.loadIcon(path : String) {

    if (path.endsWith("pdf")) {
        setImageResource(R.drawable.ic_pdf)
    }else if(path.endsWith("doc") || path.endsWith("docx")){
        setImageResource(R.drawable.ic_doc2)
    }else {
        setImageResource(R.drawable.ic_document)
    }
}

@BindingAdapter("loadTitle")
fun TextView.loadName(path : String) {

    val pathes = path.split("/")
    val name = pathes[pathes.size-1]

    text = name
}

@BindingAdapter("loadDeclarationImage")
fun ImageView.loadDeclarationImage(attachments: List<Attachment>) {
    val types = attachments.map { it.filetype }
    if(types.contains(AttachmentType.IMAGE)){
        val index = types.indexOf(AttachmentType.IMAGE)
        loadImage(attachments[index].src)
    }else {
        //TODO load place holder for declaration
    }
}

@BindingAdapter("setState")
fun LinearLayout.setState(state : DeclarationState) {
    val image = this.findViewById<ImageView>(R.id.stateBadge)
    val text = this.findViewById<TextView>(R.id.stateText)

    when(state) {
        DeclarationState.VALIDATED -> {
            image.setImageResource(R.drawable.valide)
            text.setText(R.string.validated)
        }
        DeclarationState.ARCHIVED -> {
            image.setImageResource(R.drawable.valide)
            text.setText(R.string.archived)
        }
        DeclarationState.TREATED -> {
            image.setImageResource(R.drawable.valide)
            text.setText(R.string.treated)
        }
        DeclarationState.NOT_VALIDATED -> {
            image.setImageResource(R.drawable.under_trait)
            text.setText(R.string.not_valid)
        }
        DeclarationState.UNDER_TREATMENT -> {
            image.setImageResource(R.drawable.under_trait)
            text.setText(R.string.under_trait)
        }
        DeclarationState.REFUSED -> {
            image.setImageResource(R.drawable.refused)
            text.setText(R.string.refused)
        }
        DeclarationState.LACK_OF_INFO -> {
            image.setImageResource(R.drawable.leack_info)
            text.setText(R.string.leack_info)
        }
    }
}