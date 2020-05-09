package com.roacult.madinatic.ui.declaration.adddeclaration

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes

class SpinnerAdapter<T>(
    context : Context,
    @LayoutRes layoutRes: Int,
    list : List<T>
) : ArrayAdapter<T>(context,layoutRes,list) {

    override fun isEnabled(position: Int): Boolean = position != 0

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        val textView = view as TextView
        if( position == 0 )
            textView.setTextColor(Color.GRAY)
        else
            textView.setTextColor(Color.BLACK)

        return textView
    }
}