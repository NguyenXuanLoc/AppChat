package com.example.appchat.ui.listuser

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.ext.setImageSimple
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
    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(ctx as AppCompatActivity) - margin) / 1.5f).toInt()


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
        private var sdvIcon: SimpleDraweeView = itemView.findViewById(R.id.sdv_icon)
        private var lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        private var lblContent: TextView = itemView.findViewById(R.id.lbl_content)
        private var layoutOptions: ConstraintLayout = itemView.findViewById(R.id.layout_option)
        fun bind(model: OptionModel) {
            lblTitle.text = model.title
            lblContent.text = model.content
            sdvIcon.setImageSimple(model.urlImage, ctx as Activity)
            layoutOptions.setRatio(ctx as AppCompatActivity, 9, 10, totalMargin)

            layoutOptions.setBackgroundColor(Color.parseColor(model.background.toString()))
            layoutOptions.setOnClickListener { itemClick(model) }
            sdvIcon.setBackgroundColor(Color.parseColor(model.background.toString()))
        }
    }
}