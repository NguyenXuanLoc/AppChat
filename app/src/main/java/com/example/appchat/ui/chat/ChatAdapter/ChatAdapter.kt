package com.example.appchat.ui.chat.ChatAdapter

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.ImageModel
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.chat.photo.PhotoAdapter
import com.example.appchat.ui.me.imagedetail.ImageDetailActivity
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.facebook.drawee.view.SimpleDraweeView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import timber.log.Timber
import java.lang.Exception

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
        messagers.add(0, MessageModel())
        notifyItemInserted(0)
    }

    fun removeItemLoading() {
        isLoaderVisible = false
        var position = 0
        messagers.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible && position == 0) {
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
            holder.bind(messagers[position], position)
        }
    }

    inner class ItemLeft(itemView: View) : RecyclerView.ViewHolder(itemView), ChatAdapterView {
        private var sdvAvt: SimpleDraweeView = itemView.findViewById(R.id.sdv_avt)
        private var lblMessage: TextView = itemView.findViewById(R.id.lbl_message)
        private var sdvGif: ImageView = itemView.findViewById(R.id.sdv_gif)
        private var imgLike: ImageView = itemView.findViewById(R.id.img_like)
        private var rclPhoto: RecyclerView = itemView.findViewById(R.id.rcl_photo)
        private var photos = ArrayList<ImageModel>()
        private val adapter by lazy { PhotoAdapter(ctx, photos) { onClick(it) } }
        private val presenter by lazy { ChatAdapterPresenter(this) }
        private var isPhoto = true
        fun bind(model: MessageModel) {
            rclPhoto.adapter = adapter
            rclPhoto.layoutManager =
                GridLayoutManager(ctx, 3, GridLayoutManager.VERTICAL, false)
            rclPhoto.setHasFixedSize(true)
            with(model) {
                sdvAvt.setImageSimple(userReceiver.imageUrl, ctx)
                if (message!!.isNotEmpty()) {
                    lblMessage.visible()
                    lblMessage.text = message
                } else {
                    lblMessage.gone()
                }
                if (urlGif!!.isNotEmpty()) {
                    Glide.with(ctx).asGif().load(urlGif).into(sdvGif)
                }
                if (like == Constant.LIKE) {
                    imgLike.visible()
                } else {
                    imgLike.gone()
                }
                if (isPhoto) {
                    presenter.loadPhotos(id.toString())
                    isPhoto = false
                }
            }
        }

        private fun onClick(position: Int) {
            bundleOf(Constant.POSITION to position, Constant.INFO_IMAGE to photos).also {
                var intent = Intent(ctx, ImageDetailActivity::class.java)
                intent.putExtra(Constant.INFO_IMAGE, it)
                ctx.startActivity(intent)
            }
        }

        override fun loadPhotosSuccess(list: ArrayList<ImageModel>) {
            photos.addAll(list)
            adapter.notifyDataSetChanged()
        }

        override fun loadPhotosSuccessNull() {
            rclPhoto.gone()
        }

    }

    inner class ItemRight(itemView: View) : RecyclerView.ViewHolder(itemView), ChatAdapterView {
        private var lblMessage: TextView = itemView.findViewById(R.id.lbl_message)
        private var lblIsSend: TextView = itemView.findViewById(R.id.lbl_isSent)
        private var sdvGif: SimpleDraweeView = itemView.findViewById(R.id.sdv_gif)
        private var imgLike: ImageView = itemView.findViewById(R.id.img_like)
        private var rclPhoto: RecyclerView = itemView.findViewById(R.id.rcl_photo)
        private var layoutAudio: LinearLayout = itemView.findViewById(R.id.layout_play_audio)
        private var lblTimeAudio: TextView = itemView.findViewById(R.id.lbl_time_audio)

        private var photos = ArrayList<ImageModel>()
        private val adapter by lazy { PhotoAdapter(ctx, photos) { onClick(it) } }
        private val presenter by lazy { ChatAdapterPresenter(this) }
        private var isPhoto = true
        private var mPlayer = MediaPlayer()
        fun bind(model: MessageModel, position: Int) {
            rclPhoto.adapter = adapter
            rclPhoto.layoutManager =
                GridLayoutManager(ctx, 3, GridLayoutManager.VERTICAL, false)
            rclPhoto.setHasFixedSize(true)
            with(model) {
                if (message!!.isNotEmpty()) {
                    lblMessage.visible()
                    lblMessage.text = message
                } else {
                    lblMessage.gone()
                }
                if (urlGif!!.isNotEmpty()) {
                    Glide.with(ctx).asGif().load(urlGif).into(sdvGif)
                }
                if (like == Constant.LIKE) {
                    imgLike.visible()
                } else {
                    imgLike.gone()
                }
                /*if (position == (messagers.size - 1)) {
                    var isSend = messagers[messagers.size - 1].isSend
                    if (isSend == Constant.SENDING) {
                        lblIsSend.text = Constant.SENDING
                    } else if (isSend == Constant.RECEIVED) {
                        lblIsSend.text = Constant.RECEIVED
                    }
                    lblIsSend.visible()
                }*/
                if (isPhoto) {
                    presenter.loadPhotos(id.toString())
                    isPhoto = false
                }
                if (urlAudio!!.isNotEmpty() && duration!!.isNotEmpty()) {
                    lblTimeAudio.text = (duration!!.toLong() / 1000).toString() + "''"
//                    getDuration(mPlayer, urlAudio.toString(), lblTimeAudio)
                    layoutAudio.visible()
                }
            }
        }

        private fun onClick(position: Int) {
            bundleOf(Constant.POSITION to position, Constant.INFO_IMAGE to photos).also {
                var intent = Intent(ctx, ImageDetailActivity::class.java)
                intent.putExtra(Constant.INFO_IMAGE, it)
                ctx.startActivity(intent)
            }
        }

        override fun loadPhotosSuccess(list: ArrayList<ImageModel>) {
            Log.e("TAG", "new photos: ")
            photos.addAll(list)
            adapter.notifyDataSetChanged()
        }

        override fun loadPhotosSuccessNull() {
            rclPhoto.gone()
        }


    }

    private fun getDuration(player: MediaPlayer, url: String, lblTime: TextView) {
        player.reset()
        player.setDataSource(url)
        try {
            player.prepare()
        } catch (e: Exception) {
            Timber.e(e.message)
        }
        player.setOnPreparedListener { it ->
            var duration = it.duration
            var seconds = duration % 60;
            lblTime.text = "${seconds}''"
        }
    }

    inner class ItemLoading(itemView: View) : RecyclerView.ViewHolder(itemView)

}