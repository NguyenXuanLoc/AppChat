package com.example.appchat.ui.test

import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.util.NotifyUnit
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.activity_test.*


class Test2Activity : BaseActivity() {
    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
    }

    override fun eventHandle() {
        btnStart.setOnClickListener {
            toast("Show")
            sendNotifyVoiceCall()
        }
    }

    fun sendNotifyVoiceCall() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotifyUnit.createNotificationChannel(Constant.CHANNEL_ID, "Notify", self)
            } else {
                ""
            }
        var notification = NotificationCompat.Builder(self, channelId)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notification.setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setAutoCancel(true) // auto cancel when user click to notification
            .setPriority(NotificationCompat.PRIORITY_MAX) // mức độ ưu tiên
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        with(NotificationManagerCompat.from(self)) {
            notify(0, notification.build())
        }
    }

}