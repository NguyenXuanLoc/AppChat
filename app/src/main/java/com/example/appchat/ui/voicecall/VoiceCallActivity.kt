package com.example.appchat.ui.voicecall

import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.IBinder
import android.util.Log
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.KeyPermission
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.common.util.PermissionUtil
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.voicecall.handleCall.VoiceCallService
import com.example.fcm.common.ext.*
import com.google.android.exoplayer2.extractor.VorbisUtil
import com.sinch.android.rtc.PushPair
import com.sinch.android.rtc.Sinch
import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.calling.CallClient
import com.sinch.android.rtc.calling.CallClientListener
import com.sinch.android.rtc.calling.CallListener
import kotlinx.android.synthetic.main.activity_voice_call.*


// SinchCallClientView: handle client, SinchCallListener: handle at progress is calling
class VoiceCallActivity : BaseActivity(), VoiceCallView, CallClientListener, CallListener {
    private val presenter by lazy { VoiceCallPresenter(this) }
    private var userRecipient: UserModel? = null
    private var call: Call? = null
    private lateinit var sinchClient: SinchClient
    private var callerId: String? = null
    private var recipientId: String? = null

    // isCheckCall to check between send and receive
    // if == true-> receive else == false -> send
    private var isCheckCall: Boolean? = null

    override fun contentView(): Int {
        return R.layout.activity_voice_call
    }

    override fun init() {
        hideToolbarBase()
        if (isCheckCall == true) {
            recipientId = "1"
            callerId = "2"
        } else if (isCheckCall == false) {
            recipientId = "2"
            callerId = "1"
        }
        sinchClient = Sinch.getSinchClientBuilder()
            .context(this)
            .userId(callerId)
            .applicationKey(Constant.APP_KEY)
            .applicationSecret(Constant.APP_SECRET)
            .environmentHost(Constant.ENVIRONMENT)
            .build()

        sinchClient.setSupportCalling(true)
        sinchClient.startListeningOnActiveConnection()
        sinchClient.start()
        sinchClient.callClient.addCallClientListener(this)

    }

    override fun eventHandle() {
        getUser()?.let {
            presenter.checkNode(it.id.toString(), userRecipient?.id.toString())
        }
        // check who is send, who is receive
        isCheckCall?.let { it ->
            if (it) {
                imgCall.gone()
                layoutReceive.visible()
            } else {
                imgCall.visible()
                layoutReceive.gone()
            }
        }
        imgCall.setOnClickListener {
            if (checkPermission()) {
                if (call == null) {
                    lblStatus.text = getString(R.string.waiting_answer)
                    call = sinchClient.callClient.callUser(recipientId)
                    call!!.addCallListener(this)
                } else {
                    call!!.hangup()
                }
            }
        }
        imgReceive.setOnClickListener {
            if (checkPermission()) {
                if (call == null) {
                    call = sinchClient!!.callClient.callUser(recipientId)
                    call!!.addCallListener(this)
                } else {
                    call!!.hangup()
                }
            }
        }
        imgDeject.setOnClickListener {
            call?.hangup()
        }
        imgStop.setOnClickListener { call?.hangup() }
        imgMic.setOnClickListener {
            val audioManager =
                applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.mode = AudioManager.MODE_IN_CALL
            audioManager.isMicrophoneMute = !audioManager.isMicrophoneMute
        }
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.USER)
        bundle?.let {
            userRecipient = it.getSerializable(Constant.USER) as UserModel?
            isCheckCall = it.getBoolean(Constant.CHECK_CALL)
            sdvAvt.setImageSimple(userRecipient?.imageUrl.toString(), self)
        }
    }

    private fun checkPermission(): Boolean {
        var isCheck = false
        if (PermissionUtil.isGranted(
                self,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                KeyPermission.RC_RECORD_AUDIO
            ) && PermissionUtil.isGranted(
                self,
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE
                ), KeyPermission.RC_READ_PHONE_STATE
            )
        ) {
            isCheck = true
        }
        return isCheck
    }

    override fun onIncomingCall(p0: CallClient?, p1: Call?) {
        imgCall.gone()
        layoutReceive.gone()
        layoutOpCalling.visible()
        call = p1
        call?.answer()
        call?.addCallListener(this)
    }

    override fun onCallProgressing(p0: Call?) {
        lblStatus.text = getString(R.string.collecting)
    }

    override fun onCallEstablished(p0: Call?) {
        lblStatus.text = "Bắt đầu"
        layoutOpCalling.visible()
        layoutReceive.gone()
        imgCall.gone()
        cmtTime.visible()
        cmtTime.start()
    }

    override fun onCallEnded(p0: Call?) {
        imgCall.visible()
        layoutOpCalling.gone()
        lblStatus.text = "Kết thúc cuộc gọi"
        cmtTime.stop()
        call = null
    }

    override fun onShouldSendPushNotification(p0: Call?, p1: MutableList<PushPair>?) {
        toast("Nitification")
    }

}