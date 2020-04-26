package com.roacult.madinatic.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso



@BindingAdapter("app:srcCompat")
fun ImageView.loadDrawable(drawable: Drawable){
    setImageDrawable(drawable)
}

@BindingAdapter("showInt")
fun TextView.showInt(int :Int){
    text = int.toString()
}

fun ImageView.loadImage(url:String){
    Picasso.get().load(url).into(this)
}