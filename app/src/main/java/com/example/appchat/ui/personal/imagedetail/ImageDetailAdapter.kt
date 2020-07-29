package com.example.appchat.ui.personal.imagedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.appchat.R
import com.example.appchat.data.model.ImageModel
import com.facebook.drawee.view.SimpleDraweeView


class ImageDetailAdapter(var ctx: Context, var images: ArrayList<ImageModel>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.item_image_detail, container, false)
        val sdvImage: SimpleDraweeView = view.findViewById(R.id.sdv_image)
        sdvImage.setImageURI(images[position].url)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


}