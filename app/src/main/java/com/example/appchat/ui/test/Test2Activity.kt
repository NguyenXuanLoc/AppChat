package com.example.appchat.ui.test

import android.annotation.SuppressLint
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.util.NotifyUtil
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.fcm.APIService
import com.example.appchat.ui.fcm.Client
import com.example.appchat.ui.fcm.Data
import com.example.appchat.ui.fcm.NotificationSender
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import timber.log.Timber


class Test2Activity : BaseActivity() {
    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
    }

    override fun eventHandle() {
        btnStart.setOnClickListener {
//            Log.e("TAG", "Crap")
//            throw RuntimeException("Test Crash") // Force a crash
            /* toast("Show")
            sendNotifyVoiceCall()*/
            pushNotifyCallVideo(nameSender = "Á", message = "S", idSender = "sd")
        }
    }

    @SuppressLint("CheckResult")
    fun pushNotifyCallVideo(
        userToken: String = "dLnOF6KMRKaxt25YMHNw7y:APA91bF1pIIRPU8aEuEXl7dymrlgum0SIZRQ55elY5lVVKgf6ArE57k8jGA1wdVt4TgFZTE8A5WWQ5CJPPgir7KezhAYC39GJWvYBBmm6FnhRNJ1uIq3RBOcnGmcerKrY6suFex1iRXe",
        nameSender: String,
        message: String,
        idSender: String
    ) {
        val data = Data(nameSender, message, idSender, Key.VOICE_CALL)
        val sender = NotificationSender(data, userToken)
        var client = Client()
        var apiService = client.getClient(Constant.URL_FCM)!!.create(APIService::class.java)
        apiService?.sendNotification(sender)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                if (it.success != 1) {
                    Log.e("TAG","FASLE")
                    Timber.e("False push notification")
                }else{
                    Log.e("TAG","SUCCESS")
                }
            }, {
                Timber.e(it.message)
            })
    }

    private fun sendNotifyVoiceCall() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotifyUtil.createNotificationChannel(Constant.CHANNEL_ID, "Notify", self)
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