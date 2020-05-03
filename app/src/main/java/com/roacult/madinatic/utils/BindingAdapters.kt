package com.roacult.madinatic.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("showInt")
fun TextView.showInt(int :Int){
    text = int.toString()
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(url:String?){
    if(url != null && url.isNotEmpty() ) Picasso.get().load(url).into(this)
}