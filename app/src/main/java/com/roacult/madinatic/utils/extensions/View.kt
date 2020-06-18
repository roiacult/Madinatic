package com.roacult.madinatic.utils.extensions

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun Boolean.showView() = if (this) View.VISIBLE else View.GONE

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}