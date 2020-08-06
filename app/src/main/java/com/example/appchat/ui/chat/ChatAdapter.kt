package com.example.appchat.ui.chat

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.facebook.drawee.view.SimpleDraweeView
import find

class ChatAdapter(
    var messagers: ArrayList<MessageModel>,
    var ctx: Activity, var userReceiver: UserModel,
    var userSend: UserModel, var itemClick: (model: MessageModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_LEFT = 0
    private val VIEW_TYPE_RIGHT = 1
    private val VIEW_TYLE_LOADING = 2
    private var isLoaderVisible = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LEFT -> {
                var view = LayoutInflater.from(ctx).inflate(R.layout.item_chat_left, parent, false)
                ItemLeft(view)
            }
            VIEW_TYPE_RIGHT -> {
                var view = LayoutInflater.from(ctx).inflate(R.layout.item_chat_right, parent, false)
                ItemRight(view)
            }
            else -> {
                var view = LayoutInflater.from(ctx).inflate(R.layout.layout_loadmore, parent, false)
                return ItemLoading(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return messagers.size
    }

    fun addItemLoading() {
        isLoaderVisible = true
        messagers.add(MessageModel())
        notifyItemInserted(messagers.size - 1)
    }

    fun removeItemLoading() {
        isLoaderVisible = false
        var position = messagers.size - 1
        messagers.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible && position == (messagers.size - 1)) {
            VIEW_TYLE_LOADING
        } else {
            if (messagers[position].sender == userSend.id) {
                VIEW_TYPE_RIGHT
            } else {
                VIEW_TYPE_LEFT
            }
        }

        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemLeft) {
            holder.bind(messagers[position])
        } else if (holder is ItemRight) {
            holder.bind(messagers[position])
        }
    }

    inner class ItemLeft(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var sdvAvt: SimpleDraweeView = itemView.findViewById(R.id.sdv_avt)
        private var lblMessage: TextView = itemView.findViewById(R.id.lbl_message)
        fun bind(model: MessageModel) {
            sdvAvt.setImageSimple(userReceiver.imageUrl, ctx)
            lblMessage.text = model.message
        }
    }

    inner class ItemRight(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var lblMessage: TextView = itemView.find(R.id.lbl_message)
        fun bind(model: MessageModel) {
            lblMessage.text = model.message
        }
    }

    inner class ItemLoading(itemView: View) : RecyclerView.ViewHolder(itemView)
}