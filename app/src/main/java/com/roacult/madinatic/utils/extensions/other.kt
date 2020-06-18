package com.roacult.madinatic.utils.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.roacult.domain.entities.*
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.utils.GOOGLE_MAP_BASE_URl
import com.roacult.madinatic.utils.UserBunndle
import java.util.regex.Pattern


fun putStringsInBundle(vararg pairs: Pair<String, String >): Bundle {
    return Bundle().apply {
        pairs.forEach {
            putString(it.first, it.second)
        }
    }
}

fun String.isValidEmail(): Boolean {
    val emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    val pattern = Pattern.compile(emailPattern)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isDateValid() : Boolean{
    return this.matches(Regex("\\d{4}-\\d{1,2}-\\d{1,2}"))
}

// TODO finish the bundle part
fun <T:Activity>Fragment.navigateTo(finish:Boolean = false , destinationActivity:Class<T> , bundle: Bundle?= null ){
    val intent = Intent(activity , destinationActivity)
    startActivity(intent)
    if(finish) activity?.finish()
}

fun List<String>.extractNonBlanck():List<String> {
    val list = ArrayList<String>()

    for (item in this){
        if(item.trim().isNotEmpty()){
            list.add(item)
        }
    }

    return list
}

fun User.toBunndle() : Bundle {
    return  Bundle().apply {
        putString(UserBunndle.IDU,this@toBunndle.idu)
        putString(UserBunndle.FIRSTNAME,this@toBunndle.first_name)
        putString(UserBunndle.LASTNAME,this@toBunndle.last_name)
        putString(UserBunndle.EMAIL,this@toBunndle.email)
        putString(UserBunndle.PHONE,this@toBunndle.phone)
        putString(UserBunndle.ADDRESS,this@toBunndle.address)
        putString(UserBunndle.DATEBIRTH,this@toBunndle.dateBirth)
        putString(UserBunndle.IMAGE,this@toBunndle.image)
        putString(UserBunndle.CREATEDON,this@toBunndle.created_on)
    }
}

fun Bundle.toUser() : User {
    return User(
        getString(UserBunndle.IDU)!!,
        getString(UserBunndle.FIRSTNAME)!!,
        getString(UserBunndle.LASTNAME)!!,
        getString(UserBunndle.IMAGE),
        getString(UserBunndle.PHONE)!!,
        getString(UserBunndle.DATEBIRTH)!!,
        getString(UserBunndle.EMAIL)!!,
        getString(UserBunndle.ADDRESS)!!,
        getString(UserBunndle.CREATEDON)!!
    )
}

fun String.toAttachment() : Attachment {

    val type = if(this.endsWith(".pdf")) AttachmentType.PDF
    else if (
        endsWith(".png") ||
        endsWith(".PNG") ||
        endsWith(".jpg") ||
        endsWith(".JPG") ||
        endsWith(".gif") ||
        endsWith(".GIF")
    ) AttachmentType.IMAGE
    else AttachmentType.OTHER

    return Attachment(
        "","","",type,this
    )
}

fun<D : ViewDataBinding> BaseFragment<D>.openGoogleMap(lat: Double,long: Double) {
    val gmmIntentUri = Uri.parse("geo:$lat,$long")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mapIntent)
}

fun GeoCoordination.getGoogleMapUrl() : String =
    GOOGLE_MAP_BASE_URl+this.lat+","+this.long
