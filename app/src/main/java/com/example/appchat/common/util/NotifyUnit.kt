package com.example.appchat.common.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.chat.ChatActivity
import com.example.appchat.ui.fcm.Data
import com.example.appchat.ui.voicecall.VoiceCallActivity
import com.example.appchat.ui.voicecall.handleCall.NotificationRecevier
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

object NotifyUnit : HandleNotifyListener {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        channelId: String,
        channelName: String,
        context: Context
    ): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    fun sendNotifyNewMessage(ctx: Context, model: Data) {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(Constant.CHANNEL_ID, "My Background Service", ctx)
            } else {
                ""
            }

        var notification = NotificationCompat.Builder(ctx, channelId)
        getUserRequest(model.idSender, this, notification, ctx)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notification.setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentText(model.message)
            .setPriority(NotificationCompat.PRIORITY_MAX) // mức độ ưu tiên
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
    }

    // Custom notification
    fun test(ctx: Context, model: Data) {
        var expandedView =
            RemoteViews(ctx.packageName, R.layout.layout_notification_call_expanded)
        var collapsedView =
            RemoteViews(ctx.packageName, R.layout.layout_notification_call_collapsed)
        var notification = NotificationCompat.Builder(ctx, Constant.CHANNEL_ID)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // setValue for expandedView and collapsedView
        expandedView.setTextViewText(R.id.lblNameSend, model.title)
        collapsedView.setTextViewText(R.id.lblNameSend, model.title)

        expandedView.setOnClickPendingIntent(
            R.id.lblReceive,
            onButtonNotificationClick(R.id.lblReceive, ctx, model.idSender)
        )
        expandedView.setOnClickPendingIntent(
            R.id.lblDeject,
            onButtonNotificationClick(R.id.lblDeject, ctx, model.idSender)
        )

        var intent = Intent(ctx, VoiceCallActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(ctx, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        notification.setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setAutoCancel(true) // auto cancel when user click to notification
            .setPriority(NotificationCompat.PRIORITY_MAX) // mức độ ưu tiên
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(defaultSoundUri)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        with(NotificationManagerCompat.from(ctx)) {
            notify(Constant.NOTIFY_VOICE_CALL, notification.build())
        }
    }

    fun sendNotifyVoiceCall(ctx: Context, model: Data) {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(Constant.CHANNEL_ID, "Notify", ctx)
            } else {
                ""
            }
        var notification = NotificationCompat.Builder(ctx, channelId)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        getUserRequest(model.idSender, this, notification, ctx, false)
        notification.setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setAutoCancel(true) // auto cancel when user click to notification
            .setPriority(NotificationCompat.PRIORITY_MAX) // mức độ ưu tiên
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

    }

    private fun onButtonNotificationClick(
        @IdRes id: Int,
        ctx: Context,
        idSend: String
    ): PendingIntent? {
        val intent = Intent(ctx, NotificationRecevier::class.java)
        intent.putExtra(Constant.EXTRA_BUTTON_CLICKED, id)
        intent.putExtra(Constant.ID_SEND, idSend)
        return PendingIntent.getBroadcast(ctx, id, intent, 0)
    }

    // if is check=true -> get user send message else get user call
    private fun getUserRequest(
        idUser: String, v: HandleNotifyListener,
        notification: NotificationCompat.Builder,
        ctx: Context, isCheck: Boolean = true
    ) {
        FirebaseDatabase.getInstance().getReference(Key.USER).child(idUser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var userModel = snapshot.getValue<UserModel>()
                    userModel?.let {
                        if (isCheck) {
                            v.loadUserSendMessage(it, ctx, notification)
                        } else {
                            v.loadUserVoiceCall(it, ctx, notification)
                        }
                    }
                }

            })
    }

    override fun loadUserSendMessage(
        userModel: UserModel, ctx: Context, notification: NotificationCompat.Builder
    ) {
        var intent = Intent(ctx, ChatActivity::class.java)
        bundleOf(Constant.MESSAGE to userModel).also {
            intent.putExtra(Constant.MESSAGE, it)
        }
        var pendingIntent = PendingIntent.getActivity(ctx, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        notification.setContentIntent(pendingIntent)
        notification.setContentTitle(userModel.userName)
        with(NotificationManagerCompat.from(ctx)) {
            notify(0, notification.build())
        }
    }

    override fun loadUserVoiceCall(
        userModel: UserModel,
        ctx: Context,
        notification: NotificationCompat.Builder
    ) {
        var intent = Intent(ctx, VoiceCallActivity::class.java)
        bundleOf(Constant.USER to userModel, Constant.CHECK_CALL to true).also {
            intent.putExtra(Constant.USER, it)
        }
        var pendingIntent = PendingIntent.getActivity(ctx, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        notification.setContentTitle(userModel.userName)
            .setContentText(ctx.getString(R.string.audio_invitations))
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(ctx)) {
            notify(0, notification.build())
        }
    }

}

interface HandleNotifyListener {
    fun loadUserSendMessage(
        userModel: UserModel,
        ctx: Context,
        notification: NotificationCompat.Builder
    )

    fun loadUserVoiceCall(
        userModel: UserModel,
        ctx: Context,
        notification: NotificationCompat.Builder
    )
}