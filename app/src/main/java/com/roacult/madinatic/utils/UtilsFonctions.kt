package com.roacult.madinatic.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.PagedList
import java.text.SimpleDateFormat
import java.util.*


fun Context.isConnected(): Boolean =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnectedOrConnecting == true

fun getPagedListConfig(): PagedList.Config{
    return PagedList.Config.Builder().apply {
        setEnablePlaceholders(false)
        setInitialLoadSizeHint(10)
        setPageSize(10)
    }.build()
}

fun getDateTimeFromTimeStamp(mDateFormat: String): String {
    val dateFormat = SimpleDateFormat(mDateFormat, Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val today = Calendar.getInstance().time
    return dateFormat.format(today)
}