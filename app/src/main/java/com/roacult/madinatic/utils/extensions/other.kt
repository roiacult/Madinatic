package com.roacult.madinatic.utils.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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