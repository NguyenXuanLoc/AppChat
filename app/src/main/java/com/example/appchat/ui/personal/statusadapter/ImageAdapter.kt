package com.example.appchat.ui.personal.statusadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.ImageModel
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.util.CommonUtil
import com.facebook.drawee.view.SimpleDraweeView

class ImageAdapter(
    var ctx: Context, var images: ArrayList<ImageModel>,
    var itemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ItemHolder>() {
    private val margin = CommonUtil.convertDpToPixel(
        ctx,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x, R.dimen.dimen_2x)
    )
    private val screenWidth = CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity)

    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity) - margin) / 2f).toInt()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (itemCount > 0) {
            holder.bind(images[position], position)
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sdvImage: SimpleDraweeView =
            itemView.findViewById(R.id.sdv_image)

        fun bind(model: ImageModel, position: Int) {
            sdvImage.setImageURI(model.url)
            sdvImage.setRatio(ctx as AppCompatActivity, 9, 9, totalMargin)
            sdvImage.setOnClickListener {
//                bundleOf(Constant.INFO_IMAGE to images).also {
                    itemClick(position)
//                }
            }
        }
    }

    fun updateThumbRatio(isListView: Boolean) {
        totalMargin = if (isListView) {
            screenWidth / 2 + (margin - margin * 0.25).toInt()
        } else {
            margin
        }
    }
}