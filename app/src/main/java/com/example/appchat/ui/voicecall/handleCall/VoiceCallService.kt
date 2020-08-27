package com.example.appchat.ui.voicecall.handleCall

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.KeyPermission
import com.example.appchat.common.util.PermissionUtil
import com.example.appchat.data.model.UserModel
import com.example.appchat.widget.Test
import com.sinch.android.rtc.PushPair
import com.sinch.android.rtc.Sinch
import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.calling.CallClient
import com.sinch.android.rtc.calling.CallClientListener
import com.sinch.android.rtc.calling.CallListener

class VoiceCallService : Service(), CallClientListener, CallListener {
    private var call: Call? = null
    private var sinchClient: SinchClient? = null
    private var callerId: String? = null
    private var recipientId: String? = null
    private var isCheckCall: Boolean? = null
    private var userRecipient: UserModel? = null
    private val iBinder = VoiceCallBinder()

    override fun onCreate() {
        Log.e("TAG", "ON CREATE")
        notifyBackground()
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        var bundle = intent?.getBundleExtra(Constant.USER)
        bundle?.let {
            userRecipient = it.getSerializable(Constant.USER) as UserModel?
            isCheckCall = it.getBoolean(Constant.CHECK_CALL)
        }
        if (isCheckCall == true) {
            recipientId = "1"
            callerId = "2"
        } else if (isCheckCall == false) {
            recipientId = "2"
            callerId = "1"
        }
        callerId?.let { init(it) }
        return iBinder
    }

    fun test() {
        Log.e("TAG", "NAME: ${userRecipient}")
    }

    override fun onDestroy() {
        Log.e("TAG", "DESTROY")
        super.onDestroy()
    }

    private fun notifyBackground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }
        var intent = Intent(this, Test::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    class VoiceCallBinder : Binder() {
        fun getService(): VoiceCallService {
            return VoiceCallService()
        }
    }

    fun init(callerId: String) {
        Log.e("TAG", callerId)
        sinchClient = Sinch.getSinchClientBuilder()
            .context(this)
            .userId(callerId)
            .applicationKey(Constant.APP_KEY)
            .applicationSecret(Constant.APP_SECRET)
            .environmentHost(Constant.ENVIRONMENT)
            .build()
        sinchClient?.let {
            it.setSupportCalling(true)
            it.startListeningOnActiveConnection()
            it.start()
            it.callClient.addCallClientListener(this)
        }

    }

    override fun onIncomingCall(p0: CallClient?, p1: Call?) {
        Log.e("TAG", "ON COMING")
        call = p1
        call?.answer()
        call?.addCallListener(this)
    }

    override fun onCallProgressing(p0: Call?) {
        Log.e("TAG", "LOADING")
    }

    override fun onCallEstablished(p0: Call?) {
        Log.e("TAG", "START")
    }

    override fun onCallEnded(p0: Call?) {
        Log.e("TAG", "ENDED")
    }

    override fun onShouldSendPushNotification(p0: Call?, p1: MutableList<PushPair>?) {
    }

    fun call(recipientId: String) {
        if (checkPermission()) {
            if (call == null) {
                call = sinchClient?.callClient?.callUser(recipientId)
                call?.addCallListener(this)
            } else {
                call?.hangup()
            }
        }
    }

    fun stop() {

    }

    private fun checkPermission(): Boolean {
        var isCheck = false
        if (PermissionUtil.isGranted(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                KeyPermission.RC_RECORD_AUDIO
            ) && PermissionUtil.isGranted(
                this,
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE
                ), KeyPermission.RC_READ_PHONE_STATE
            )
        ) {
            isCheck = true
        }
        return isCheck
    }
}

interface GetDataListener {
    fun getIdCall(idSender: String, idReceive: String)
}