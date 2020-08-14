package com.example.appchat.ui.conversation.conversationadapter

import android.app.Activity
import android.util.Log
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.common.ext.getAge
import com.example.appchat.common.ext.setImage
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.common.util.TimeUtil
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.facebook.drawee.view.SimpleDraweeView
import kotlin.random.Random

@Suppress("DEPRECATION")
class ConversationAdapter(
    var user: UserModel,
    var list: ArrayList<UserModel>,
    var ctx: Activity,
    var itemClick: (UserModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_NOMAL = 0
    val ITEM_LOADING = 1
    var IS_LOADING = false

    override fun getItemViewType(position: Int): Int {
        return if (IS_LOADING) {
            when (position) {
                list.size - 1 -> ITEM_LOADING
                else -> ITEM_NOMAL
            }
        } else
            ITEM_NOMAL
    }

    fun insertLoading() {
        IS_LOADING = true
        list.add(UserModel())
        notifyItemInserted(list.size - 1)
    }

    fun removeLoading() {
        IS_LOADING = false
        var position = list.size - 1
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_NOMAL) {
            var view = LayoutInflater.from(ctx).inflate(R.layout.item_conversation, parent, false)
            ItemHolder(view)
        } else {
            var view = LayoutInflater.from(ctx).inflate(R.layout.layout_loadmore, parent, false)
            ItemLoading(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.bind(list[position])
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ConversationAdapterView {
        private val presenter by lazy { ConversationAdapterPresenter(this) }
        private var imgBackground: View = itemView.findViewById(R.id.img_background_gender)
        private var sdvAvt: SimpleDraweeView = itemView.findViewById(R.id.sdv_avt)
        private var lblName: TextView = itemView.findViewById(R.id.lbl_name)
        private var lblLastMessage: TextView = itemView.findViewById(R.id.lbl_last_message)
        private var lblAge: TextView = itemView.findViewById(R.id.lbl_age)
        private var imgGender: ImageView = itemView.findViewById(R.id.img_gender)
        private var ctlClick: ConstraintLayout = itemView.findViewById(R.id.ctl_click)
        private var imgStatus: ImageView = itemView.findViewById(R.id.img_status)
        private var lblTime: TextView = itemView.findViewById(R.id.lbl_time)
        private var lblCountUnread: TextView = itemView.findViewById(R.id.lbl_count_unread)
        fun bind(model: UserModel) {
            with(model) {
                presenter.checkNodeChild(model.id.toString(), user.id.toString())
                sdvAvt.setBackgroundResource(randomBackground())
                sdvAvt.setImageSimple(model.imageUrl, ctx)
                lblName.text = model.userName
                model.dateOfBirth?.let { it -> lblAge.getAge(it) }
                if (model.isFeMale()) {
                    imgGender.setImage(R.drawable.ic_female_white, ctx)
                } else if (model.isMale()) {
                    imgBackground.setBackgroundDrawable(ctx.getDrawable(R.drawable.bn_radius_blue))
                    imgGender.setImage(R.drawable.ic_male_white, ctx)
                }
                if (model.status == Key.OFFLINE) imgStatus.setImageDrawable(ctx.getDrawable(R.drawable.ic_dots_offline))
                ctlClick.setOnClickListener { itemClick(this) }
            }
        }

        override fun resultLastMessage(model: MessageModel) {
            lblLastMessage.text = model.message
            var date = TimeUtil.removeYear(model.date.toString())
            lblTime.text = "${model.time} $date"
        }

        override fun resultCountUnread(count: String) {
            if (count.toInt() > 0) {
                lblCountUnread.text = "+$count"
                lblCountUnread.visible()
            } else {
                lblCountUnread.gone()
            }
        }
    }

    private fun randomBackground(): Int {
        var lists = ArrayList<Int>()
        lists.add(R.drawable.bn_circle_purple_white)
        lists.add(R.drawable.bn_circle_white_grey)
        lists.add(R.drawable.bn_circle_pink)
        lists.add(R.drawable.bn_circle_red)
        var index = Random.nextInt(lists.size - 1)
        return lists[index]
    }

    inner class ItemLoading(itemView: View) : RecyclerView.ViewHolder(itemView)
}