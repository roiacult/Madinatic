package com.roacult.madinatic.utils

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.roacult.domain.entities.Announce
import com.roacult.domain.entities.Attachment
import com.roacult.domain.entities.AttachmentType
import com.roacult.domain.entities.DeclarationState
import com.roacult.madinatic.R
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("layoutManager", "mangerDetails")
fun setLayoutmanager(recyclerView: RecyclerView, layoutManager: LayoutManagerType, managerDetails: Int) {
    recyclerView.layoutManager = when (layoutManager) {
        LayoutManagerType.LINEARMANAGER -> {
            val manager = LinearLayoutManager(recyclerView.context)
            manager.orientation = managerDetails
            manager
        }
        LayoutManagerType.GRIDMANAGER -> GridLayoutManager(recyclerView.context, managerDetails)
        LayoutManagerType.STAGGERDMANAGER -> {
            StaggeredGridLayoutManager(managerDetails, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        }
    }
}

@BindingAdapter("onLongClick")
fun View.onLongClick(action : (View)->Boolean){
    this.setOnLongClickListener{ action(it) }
}

@BindingAdapter("epoxyController")
fun EpoxyRecyclerView.setRecyclerData(controller: EpoxyController) {
    setController(controller)
}

@BindingAdapter("showInt")
fun TextView.showInt(int :Int){
    text = int.toString()
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(url:String?){
    if(url != null && url.isNotEmpty() ){
        Picasso.get().load(url.replace("http","https")).into(this)
    }
}

@BindingAdapter("loadUri")
fun ImageView.loadImageUri(path : String) {
    if(path.startsWith("http"))
        loadImage(path)
    else
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

@BindingAdapter("loadicon")
fun ImageView.loadIcon(attachment : Attachment) {

    when (attachment.filetype) {
        AttachmentType.PDF -> {
            setImageResource(R.drawable.ic_pdf)
        }
        AttachmentType.IMAGE -> {
            setImageResource(R.drawable.file_image_outline)
        }
        else -> {
            setImageResource(R.drawable.ic_document)
        }
    }
}

@BindingAdapter("loadtext")
fun TextView.loadText(attachment : Attachment) {
    val paths = attachment.src.split("/")
    text = paths[paths.size-1]
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
        this.scaleType = ImageView.ScaleType.CENTER_CROP
        loadImage(attachments[index].src)
    }else {
        this.scaleType = ImageView.ScaleType.CENTER
        this.setImageResource(R.drawable.ic_place_holder)
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

@BindingAdapter("announceColor")
fun View.announceColor(announce: Announce) {
    val startDate = announce.startAt.replace('T',' ').toDate(DATE_FORMAT)
    val endDate = announce.endAt.replace('T',' ').toDate(DATE_FORMAT)

    if(startDate == null || endDate == null ) return
    val currentTime = System.currentTimeMillis()

    if(startDate.time <= currentTime && currentTime <= endDate.time){
        setBackgroundColor(Color.RED)
    }else if(startDate.time > currentTime) {
        setBackgroundColor(Color.BLUE)
    }else {
        setBackgroundColor(Color.GREEN)
    }
}