package com.example.appchat.ui.voicecall.handleCall

import android.Manifest
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
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.KeyPermission
import com.example.appchat.common.util.PermissionUtil
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.voicecall.VoiceCallActivity
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
    private var listener: VoiceCallListener? = null
    private val iBinder = VoiceCallBinder()
    private var callerId: String? = null
    private var recipientId: String? = null
    private var isCheckCall: Boolean? = null
    private var userRecipient: UserModel? = null

    override fun onCreate() {
        Log.e("TAG", "ON CREATE")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("TAG", "ON START COMMAN")
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
        userRecipient?.let { notifyBackground(it) }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
/*        callerId?.let { init(it) }*/
        return iBinder
    }

    override fun onDestroy() {
        Log.e("TAG", "DESTROY")
        super.onDestroy()
    }

    private fun notifyBackground(userModel: UserModel) {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(Constant.CHANNEL_ID, "Notify")
            } else {
                ""
            }
        var intent = Intent(this, VoiceCallActivity::class.java)
        bundleOf(
            Constant.USER to userRecipient, Constant.CHECK_CALL to isCheckCall
        ).also {
            intent.putExtra(Constant.SERVICE, it)
        }
        var pendingIntent =
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        var notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(pendingIntent)
            .setUsesChronometer(true)
            .setContentTitle(userModel.userName)
            .setContentText(this.getString(R.string.click_to_return_call))
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

    fun init(callerId: String?, context: Context) {
        Log.e("TAG", "Khởi tạo $callerId")
        sinchClient = Sinch.getSinchClientBuilder()
            .context(context)
            .userId(callerId)
            .applicationKey(Constant.APP_KEY)
            .applicationSecret(Constant.APP_SECRET)
            .environmentHost(Constant.ENVIRONMENT)
            .build()
        val let = sinchClient?.let {
            it.setSupportCalling(true)
            it.startListeningOnActiveConnection()
            it.start()
            it.callClient.addCallClientListener(this)
        }
    }

    override fun onIncomingCall(p0: CallClient?, p1: Call?) {
        listener?.onComing()
        Log.e("TAG", "ON COMING")
        call = p1
        call?.answer()
        call?.addCallListener(this)
    }

    override fun onCallProgressing(p0: Call?) {
        Log.e("TAG", "LOADING")
    }

    override fun onCallEstablished(p0: Call?) {
        listener?.onEstablish()
        Log.e("TAG", "START")
    }

    override fun onCallEnded(p0: Call?) {
        call = null
        listener?.onCallEnd()
        Log.e("TAG", "ENDED")
    }

    override fun onShouldSendPushNotification(p0: Call?, p1: MutableList<PushPair>?) {
    }

    interface VoiceCallListener {
        fun onComing()
        fun onEstablish()
        fun onCallEnd()
    }

    fun setVoiceCallListener(v: VoiceCallListener) {
        listener = v
    }

    fun startCall(recipientId: String?) {
        if (checkPermission()) {
            if (call == null) {
                Log.e("TAG", "GỌI")
                call = sinchClient?.callClient?.callUser(recipientId)
                call?.addCallListener(this)
            } else {
                Log.e("TAG", "HỦY")
                call?.hangup()
            }
        }
    }

    fun stop() {
        Log.e("TAG", "Stop")
        if (checkPermission()) {
           /* if (call == null) {
                call = sinchClient?.callClient?.callUser(recipientId)
                call?.addCallListener(this)
            } else*/
            if (call!=null){
                Log.e("TAG", "HỦY")
                call?.hangup()
            }
        }
    }

    private fun checkPermission(): Boolean {
        var isCheck = false
        if (PermissionUtil.isGranted(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                KeyPermission.RC_RECORD_AUDIO, true
            ) && PermissionUtil.isGranted(
                this,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE
                ), KeyPermission.RC_READ_PHONE_STATE
            )
        ) {
            isCheck = true
        }
        return isCheck
    }
}
