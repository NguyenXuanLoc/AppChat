package com.example.appchat.ui.chat.gif

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appchat.R
import com.example.appchat.data.model.GifModel
import com.facebook.drawee.view.SimpleDraweeView

class GifAdapter(
    var list: ArrayList<GifModel>,
    var ctx: Activity,
    var itemClick: (GifModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_NOMAL = 0
    private val VIEW_TYLE_LOADING = 1
    private var isLoaderVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NOMAL) {
            var view = LayoutInflater.from(ctx).inflate(R.layout.item_gif, parent, false)
            ItemHolder(view)
        } else {
            var view = LayoutInflater.from(ctx).inflate(R.layout.layout_loadmore, parent, false)
            ItemLoading(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            when (position) {
                list.size - 1 -> VIEW_TYLE_LOADING
                else -> VIEW_TYPE_NOMAL
            }
        } else {
            VIEW_TYPE_NOMAL
        }
        return super.getItemViewType(position)
    }

    fun addLoading() {
        isLoaderVisible = true
        list.add(GifModel())
        notifyItemInserted(list.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        var position = list.size - 1
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.bind(list[position])
        }
    }

    inner class ItemLoading(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var sdvGif: SimpleDraweeView = itemView.findViewById(R.id.sdv_gif)
        fun bind(model: GifModel) {
            Glide.with(ctx as AppCompatActivity).asGif().load(model.url).into(sdvGif)
            sdvGif.setOnClickListener { itemClick(model) }
        }
    }

}