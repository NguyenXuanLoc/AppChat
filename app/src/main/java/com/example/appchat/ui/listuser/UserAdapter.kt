package com.example.appchat.ui.listuser

import android.app.Activity
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.ext.getAge
import com.example.appchat.common.ext.setImage
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.UserModel
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import find
import kotlin.random.Random

class UserAdapter(
    var users: ArrayList<UserModel>,
    var ctx: Activity,
    var itemClick: (UserModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL) {
            var view = LayoutInflater.from(ctx).inflate(R.layout.item_user, parent, false)
            ItemHolder(view)
        } else {
            var view = LayoutInflater.from(ctx).inflate(R.layout.layout_loadmore, parent, false)
            ItemLoading(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            when (position) {
                (users.size - 1) -> VIEW_TYPE_LOADING
                else -> VIEW_TYPE_NORMAL
            }
        } else VIEW_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.bind(users[position])
        }
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = users.size - 1
        users.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addLoading() {
        isLoaderVisible = true
        users.add(UserModel())
        notifyItemInserted(users.size - 1)
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sdvAvt: SimpleDraweeView = itemView.findViewById(R.id.sdv_avt)
        var lblName: TextView = itemView.findViewById(R.id.lbl_name)
        var lblStory: TextView = itemView.findViewById(R.id.lbl_story)
        var lblAge: TextView = itemView.findViewById(R.id.lbl_age)
        var imgGender: ImageView = itemView.findViewById(R.id.img_gender)
        var ctlClick: ConstraintLayout = itemView.findViewById(R.id.ctl_click)
        fun bind(model: UserModel) {
            with(model) {
                sdvAvt.setBackgroundResource(randomBackground())
                sdvAvt.setImageSimple(model.imageUrl, ctx)
                lblName.text = model.userName
                lblStory.text = model.story
                if (model.dateOfBirth.toString().isNotEmpty())
                    lblAge.getAge(model.dateOfBirth.toString())
                if (model.isFeMale()) imgGender.setImage(R.drawable.ic_female_white, ctx)
                else imgGender.setImage(R.drawable.ic_male_white, ctx)
                ctlClick.setOnClickListener { itemClick(this) }
            }
        }
    }

    inner class ItemLoading(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun randomBackground(): Int {
        var lists = ArrayList<Int>()
        lists.add(R.drawable.bn_circle_purple_white)
        lists.add(R.drawable.bn_circle_white_grey)
        lists.add(R.drawable.bn_circle_pink)
        lists.add(R.drawable.bn_circle_red)
        var index = Random.nextInt(lists.size - 1)
        return lists[index]
    }
}