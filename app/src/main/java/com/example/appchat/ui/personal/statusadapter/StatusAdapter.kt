package com.example.appchat.ui.personal.statusadapter

import android.app.Activity
import android.content.Context
import android.os.FileUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.util.FileUtil
import com.example.appchat.data.model.StatusModel
import com.example.appchat.data.model.VideoModel
import com.example.appchat.ui.playvideo.PlayVideoActivity
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.setRatio
import com.example.fcm.common.ext.visible
import com.example.fcm.common.util.CommonUtil

class StatusAdapter(
    var self: Activity,
    var status: ArrayList<StatusModel>,
    var itemClick: (StatusModel) -> Unit
) :
    RecyclerView.Adapter<StatusAdapter.ItemHolder>(), StatusAdapterView {
    private val presenter by lazy { StatusAdapterPresenter(this) }
    private val margin = CommonUtil.convertDpToPixel(
        self,
        intArrayOf(R.dimen.dimen_2x, R.dimen.dimen_2x, R.dimen.dimen_2x)
    )
    private var totalMargin =
        (margin + (CommonUtil.getScreenWidthAsPixel(self as AppCompatActivity) - margin) / 2f).toInt()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return ItemHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return status.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (itemCount > 0) {
            holder.onBind(status[position])
        }
    }

    override fun loadVideoSuccess(videoModel: VideoModel, itemHolder: ItemHolder) {
        itemHolder.imgVideo.visible()
        itemHolder.imgPlayVideo.visible()
        itemHolder.imgVideo.setImageBitmap(FileUtil.getThumbnailFromUrl(videoModel.url, self))
        //Listener
        itemHolder.imgVideo.setOnClickListener {
            bundleOf(Constant.PLAY_VIDEO to videoModel).also {
                (self as AppCompatActivity).openActivity(
                    PlayVideoActivity::class.java,
                    it,
                    Constant.PLAY_VIDEO
                )
            }
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lblTime: TextView = itemView.findViewById(R.id.lblTime)
        var lblStatus: TextView = itemView.findViewById(R.id.lblStatus)
        var imgVideo: ImageView = itemView.findViewById(R.id.imgVideo)
        var imgPlayVideo: ImageView = itemView.findViewById(R.id.imgPlayVideo)
        fun onBind(statusModel: StatusModel) {
            lblTime.text = statusModel.date
            lblStatus.text = statusModel.status
            presenter.loadVideo(statusModel.id, this)
            imgVideo.setRatio(self as AppCompatActivity, 6, 9, totalMargin)

        }
    }
}