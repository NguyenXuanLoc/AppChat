package com.example.appchat.ui.chat.photo

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.ImageModel
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.util.CommonUtil
import com.facebook.drawee.view.SimpleDraweeView

class PhotoAdapter(
    var ctx: Activity,
    var list: ArrayList<ImageModel>,
    var onClickItem: (position: Int) -> Unit
) :
    RecyclerView.Adapter<PhotoAdapter.ItemHolder>() {
    private val margin = CommonUtil.convertDpToPixel(
        ctx,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x, R.dimen.dimen_2x)
    )

    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity) - margin) / 1.5f).toInt()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_photo, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PhotoAdapter.ItemHolder, position: Int) {
        if (itemCount > 0) {
            holder.bind(list[position], position)
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var sdvPhoto: SimpleDraweeView = itemView.findViewById(R.id.sdv_photo)
        fun bind(model: ImageModel, position: Int) {
            sdvPhoto.setImageSimple(model.url, ctx)
            sdvPhoto.setRatio(ctx as AppCompatActivity, 9, 9, totalMargin)
            sdvPhoto.setOnClickListener {
                onClickItem(position)
            }
        }
    }
}