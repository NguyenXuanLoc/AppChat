package com.example.appchat.ui.test

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.ext.setImageSimple
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.ext.visible
import com.example.fcm.common.util.CommonUtil
import com.facebook.drawee.view.SimpleDraweeView
import java.io.File

class PhotoAdapter(
    var list: List<String>,
    var ctx: Activity,
    var addPath: (uri: String) -> Unit,
    var removePath: (path: String) -> Unit
) :
    RecyclerView.Adapter<PhotoAdapter.ItemHolder>() {
    private val margin = CommonUtil.convertDpToPixel(
        ctx,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x, R.dimen.dimen_2x)
    )
    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity) - margin) / 1.5f).toInt()
    private var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.item_image_message, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (itemCount > 0) {
            holder.bind(list[position])
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var isCheck = true
        private var sdvPhoto: SimpleDraweeView = itemView.findViewById(R.id.sdv_photo)
        private var imgBackground: ImageView = itemView.findViewById(R.id.img_background)
        private var imgTick: ImageView = itemView.findViewById(R.id.img_tick)
        fun bind(path: String) {
            sdvPhoto.setRatio(ctx as AppCompatActivity, 10, 10, totalMargin)
            sdvPhoto.setImageSimple(File(path), ctx)
            sdvPhoto.setOnClickListener {
                if (isCheck) {
                      if (count >= Constant.MAX_PHOTO) {
                          Toast.makeText(
                              ctx,
                              ctx.resources.getString(R.string.max_night_photo_notify),
                              Toast.LENGTH_SHORT
                          ).show()
                      }
                      else {
                    addPath(path)
                    imgBackground.visible()
                    imgTick.visible()
                    count++
                    isCheck = false
                    }
                } else {
                    removePath(path)
                    imgBackground.gone()
                    imgTick.gone()
                    count--
                    isCheck = true
                }
            }
        }
    }

    private fun updateCount() {

    }
}