package com.roacult.madinatic.ui.declaration.declarationdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.roacult.madinatic.R
import com.squareup.picasso.Picasso

class DeclarationSlideShowAdapter(
    private val context: Context
) : PagerAdapter() {

    private val images = ArrayList<String>()

    override fun isViewFromObject(view: View, `object`: Any) = view == (`object` as ImageView)

    override fun getCount(): Int {
        return maxOf(1,images.size)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.declaration_slide_show, container, false) as ImageView

        if(images.isEmpty()) {
            view.scaleType = ImageView.ScaleType.CENTER
            view.setImageResource(R.drawable.ic_place_holder)
        }else {
            Picasso.get().load(images[position]).into(view)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ImageView)
    }

    fun setImages(newImages: List<String>) {
        images.clear()
        images.addAll(newImages)
        this.notifyDataSetChanged()
    }
}