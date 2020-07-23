package com.example.appchat.ui.personal.uploadstatus

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.util.CommonUtil
import com.facebook.drawee.view.SimpleDraweeView

class UploadStatusImageAdapter(
    var ctx: Context,
    var listUri: ArrayList<Uri>,
    var itemClick: (posittion: Int) -> Unit
) : RecyclerView.Adapter<UploadStatusImageAdapter.ItemHolder>() {

    private val margin = CommonUtil.convertDpToPixel(
        ctx,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x)
    )
    private val screenWidth = CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity)
    private var totalMargin = margin
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.item_status_image, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return listUri.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (itemCount > 0) {
            holder.bind(listUri[position], position)
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image = itemView.findViewById<SimpleDraweeView>(R.id.sdv_image)
        private var imgDelete = itemView.findViewById<ImageView>(R.id.img_delete)
        fun bind(uri: Uri, position: Int) {
            image.setRatio(ctx as AppCompatActivity, 16,9, totalMargin)
            image.setImageURI(uri)
            imgDelete.setOnClickListener { itemClick(position) }
        }
    }


    fun updateThumbRatio(isListView: Boolean) {
        totalMargin = if (isListView) {
            screenWidth / 3 + (margin - margin * 0.25).toInt()
        } else {
            margin
        }
    }
}