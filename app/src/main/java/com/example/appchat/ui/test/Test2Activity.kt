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
import com.example.appchat.ui.fcm.*
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import timber.log.Timber


class Test2Activity : BaseActivity() {
    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
        updateToken()
    }

    private fun updateToken() {
        val refreshToken = FirebaseInstanceId.getInstance().token
        Log.e("TAG", "TOKEN: ${refreshToken.toString()}");
        val token = refreshToken?.let { Token(it) }
        FirebaseDatabase.getInstance().getReference(Key.TOKENS)
            .child(getUser()?.id.toString()).setValue(token)
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
        userToken: String = "ekAQ8COORgCQyWYXBzEpEs:APA91bFNRqdJvtR5nR5Xnc-c72fiChvyG4W-HTWridqf6tvi4FXqIgXxRNwHInphhRfwhYeDJnZuL9Uko19IKHwK-cNVG2ch-0ATCJHvgaLp2aTpxlrpSoV50IMgCPJR9LKszHz8Chw9",
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
                    Log.e("TAG", "FASLE")
                    Timber.e("False push notification")
                } else {
                    Log.e("TAG", "SUCCESS")
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