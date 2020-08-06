package com.example.appchat.ui.listuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.data.model.OptionModel
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.util.CommonUtil
import com.facebook.drawee.view.SimpleDraweeView

class OptionAdapter(
    var lists: ArrayList<OptionModel>,
    var ctx: Context,
    var itemClick: (model: OptionModel) -> Unit
) : RecyclerView.Adapter<OptionAdapter.ItemHolder>() {
    private val margin = CommonUtil.convertDpToPixel(
        ctx,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x, R.dimen.dimen_2x)
    )
    private val screenWidth = CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity)
    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity) - margin) / 2f).toInt()

    fun updateThumbRatio(isListView: Boolean) {
        totalMargin = if (isListView) {
            screenWidth / 2 + (margin - margin * 0.25).toInt()
        } else {
            margin
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view = LayoutInflater.from(ctx).inflate(R.layout.item_option, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (itemCount > 0) {
            holder.bind(lists[position])
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sdvIcon: SimpleDraweeView = itemView.findViewById(R.id.sdv_icon)
        var cvOption: CardView = itemView.findViewById(R.id.cv_option)
        var lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        var lblContent: TextView = itemView.findViewById(R.id.lbl_content)
        fun bind(model: OptionModel) {
//            sdvIcon.setRatio(ctx as AppCompatActivity, 9, 9, totalMargin)
//cvOption.setBac
        }
    }
}