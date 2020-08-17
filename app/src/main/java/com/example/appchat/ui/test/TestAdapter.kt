package com.example.appchat.ui.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appchat.R
import com.example.appchat.data.model.ImageModel

class TestAdapter(
    var ctx: Context,
    var images: ArrayList<ImageModel>,
    var onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<TestAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
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
        var sdvTest: ImageView = itemView.findViewById<ImageView>(R.id.sdv_test)
        fun bind(model: ImageModel, position: Int) {
            Glide.with(ctx).load(model.url).into(sdvTest);
//            sdvTest.setOnClickListener { onClick(position) }
        }
    }
}