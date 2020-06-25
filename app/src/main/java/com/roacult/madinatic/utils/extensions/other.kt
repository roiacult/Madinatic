package com.roacult.madinatic.utils.extensions

import android.os.Bundle
import com.roacult.domain.entities.*
import com.roacult.madinatic.utils.GOOGLE_MAP_BASE_URl
import com.roacult.madinatic.utils.UserBunndle
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean {
    val emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    val pattern = Pattern.compile(emailPattern)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.isDateValid() : Boolean{
    return this.matches(Regex("\\d{4}-\\d{1,2}-\\d{1,2}"))
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

fun GeoCoordination.getGoogleMapUrl() : String =
    GOOGLE_MAP_BASE_URl+this.lat+","+this.long
