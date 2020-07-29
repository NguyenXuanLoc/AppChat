package com.example.appchat.ui.personal.statusadapter

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.ImageModel
import com.example.appchat.data.model.StatusModel
import com.example.appchat.ui.personal.imagedetail.ImageDetailActivity
import com.example.appchat.ui.playvideo.PlayVideoActivity
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.ext.visible
import com.example.fcm.common.util.CommonUtil
import com.facebook.drawee.view.SimpleDraweeView
import find

class StatusAdapter(
    var self: Activity,
    var status: ArrayList<StatusModel>,
    var itemClick: (StatusModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false

    private val margin = CommonUtil.convertDpToPixel(
        self,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x, R.dimen.dimen_2x)
    )
    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(self as AppCompatActivity) - margin) / 2f).toInt()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
            ItemHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.layout_loadmore, parent, false)
            LoadMoreHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return status.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            when (position) {
                status.size - 1 -> VIEW_TYPE_LOADING
                else -> VIEW_TYPE_NORMAL
            }
        } else {
            VIEW_TYPE_NORMAL
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemCount > 0) {
            if (holder is ItemHolder) {
                holder.onBind(status[position])
            }
        }
    }


    fun addLoadingView() {
        isLoaderVisible = true
        status.add(StatusModel())
        notifyItemInserted(status.size - 1)
    }

    fun removeLoadingView() {
        isLoaderVisible = false
        val position = status.size - 1
        status.removeAt(position)
        notifyItemRemoved(position)
    }

    @Suppress("DEPRECATION")
    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), StatusAdapterView {
        private var lblTime: TextView = itemView.findViewById(R.id.lblTime)
        private var lblStatus: TextView = itemView.findViewById(R.id.lblStatus)
        private var sdvVideo: SimpleDraweeView = itemView.findViewById(R.id.sdvVideo)
        private var imgPlayVideo: ImageView = itemView.findViewById(R.id.imgPlayVideo)
        private var layoutPlayAudio: LinearLayout = itemView.findViewById(R.id.layoutPlayAudio)
        private var lvAudio: LottieAnimationView = itemView.findViewById(R.id.lvAudio)
        private var lblTimeAudio: TextView = itemView.findViewById(R.id.lblTimeAudio)
        private var mediaPlayer = MediaPlayer()

        private var rclImage: RecyclerView = itemView.findViewById(R.id.rclImage)
        private var images = ArrayList<ImageModel>()
        private var adapter = ImageAdapter(
            self,
            images
        ) { clickImage(it) }
        private val presenter by lazy { StatusAdapterPresenter(this) }
        fun onBind(statusModel: StatusModel) {
            with(statusModel) {
                lblTime.text = "$time, $date"
                status?.run {
                    lblStatus.text = this
                    lblStatus.visible()
                }
                sdvVideo.setRatio(self as AppCompatActivity, 9, 16, totalMargin)
                if (video.isNotEmpty()) {
                    imgPlayVideo.visible()
                    sdvVideo.visible()
                    sdvVideo.setImageURI(thumbnail)
                    sdvVideo.setOnClickListener {
                        bundleOf(Constant.PLAY_VIDEO to this.video).also {
                            (self as AppCompatActivity).openActivity(
                                PlayVideoActivity::class.java,
                                it,
                                Constant.PLAY_VIDEO
                            )
                        }
                    }
                }
                if (audio.isNotEmpty()) {
//                    lvAudio.playAnimation()
//                    mediaPlayer.reset()
//                    mediaPlayer.setDataSource(audio)
//                    mediaPlayer.prepare()
//                    timeDuration(mediaPlayer.duration.toLong(), mediaPlayer, audio)
                    layoutPlayAudio.visible()
                    layoutPlayAudio.setOnClickListener {
                        lvAudio.playAnimation()
                        playAudio(mediaPlayer, audio)
                    }
                }
                rclImage.adapter = adapter
                adapter.updateThumbRatio(true)
                var manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                rclImage.layoutManager = manager
                presenter.loadImage(statusModel.id)
            }
        }

        override fun loadImageSuccess(results: ImageModel) {
            if (results != null) {
                images.add(results)
                adapter.notifyDataSetChanged()
            }
        }

        private fun clickImage(position: Int) {
            bundleOf(Constant.POSITION to position, Constant.INFO_IMAGE to images).also {
                var intent = Intent(self, ImageDetailActivity::class.java)
                intent.putExtra(Constant.INFO_IMAGE, it)
                self.startActivity(intent)
            }
        }
    }

    inner class LoadMoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

private fun timeDuration(time: Long, mediaPlayer: MediaPlayer, url: String) {
    mediaPlayer.reset()
    mediaPlayer.setDataSource(url)
    mediaPlayer.prepare()
    var seconds: Int = (((time % (1000 * 60 * 60)) / (1000 * 60)) / 1000).toInt()
    Log.e("TAG", seconds.toString())
}

private fun playAudio(mediaPlayer: MediaPlayer, url: String) {
    mediaPlayer.reset()
    mediaPlayer.setDataSource(url)
    try {
        mediaPlayer.prepare()
    } catch (e: Exception) {
        Log.e("TAG", e.message)
    }
    mediaPlayer.start()
}
