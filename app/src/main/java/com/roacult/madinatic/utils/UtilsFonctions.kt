package com.roacult.madinatic.utils

import android.content.Context
import android.net.ConnectivityManager


fun Context.isConnected(): Boolean =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnectedOrConnecting == true