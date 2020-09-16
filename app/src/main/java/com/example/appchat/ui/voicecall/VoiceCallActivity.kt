package com.example.appchat.ui.voicecall

import android.content.*
import android.media.AudioManager
import android.os.IBinder
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.voicecall.handleCall.VoiceCallService
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import kotlinx.android.synthetic.main.activity_voice_call.*


// SinchCallClientView: handle client, SinchCallListener: handle at progress is calling
class VoiceCallActivity : BaseActivity(), VoiceCallView, VoiceCallService.VoiceCallListener {
    private val presenter by lazy { VoiceCallPresenter(this) }
    private var userRecipient: UserModel? = null
    private var token: String? = null
    private var callerId: String? = null
    private var recipientId: String? = null
    private var isCheckCall: Boolean? = null
    private var service: VoiceCallService? = null
    private var serviceConnection: ServiceConnection? = null

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
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
                val binder: VoiceCallService.VoiceCallBinder =
                    iBinder as VoiceCallService.VoiceCallBinder
                service = binder.getService()
                service!!.init(callerId, self)
                service!!.setVoiceCallListener(this@VoiceCallActivity)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }
    }

    override fun eventHandle() {
        createService()

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
            recipientId?.let { it1 -> service?.startCall(it1) }
        }
        imgReceive.setOnClickListener {
            recipientId?.let { it1 -> service?.startCall(it1) }
        }
        imgDeject.setOnClickListener {
            service?.stop()
        }
        imgStop.setOnClickListener {
            service?.stop()
        }
        imgMic.setOnClickListener {
            val audioManager =
                applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.mode = AudioManager.MODE_IN_CALL
            audioManager.isMicrophoneMute = !audioManager.isMicrophoneMute
        }
    }

    override fun getExtra() {
        intent.getBundleExtra(Constant.USER)?.let {
            userRecipient = it.getSerializable(Constant.USER) as UserModel?
            isCheckCall = it.getBoolean(Constant.CHECK_CALL)
            sdvAvt.setImageSimple(userRecipient?.imageUrl.toString(), self)
            token = it.getString(Constant.TOKEN)
            toast("CREATE")
        }
        intent.getBundleExtra(Constant.SERVICE)?.let { it ->
            userRecipient = it.getSerializable(Constant.USER) as UserModel?
            isCheckCall = it.getBoolean(Constant.CHECK_CALL)
            sdvAvt.setImageSimple(userRecipient?.imageUrl.toString(), self)
            token = it.getString(Constant.TOKEN)

            serviceConnection?.let { it -> bindService(intent, it, BIND_AUTO_CREATE) }
            layoutOpCalling.visible()
            imgCall.gone()
            imgReceive.gone()
            imgDeject.gone()
            toast(userRecipient?.userName + "SERVICE")
        }
        if (isCheckCall == true) {
            recipientId = "1"
            callerId = "2"
        } else if (isCheckCall == false) {
            recipientId = "2"
            callerId = "1"
        }

    }

    private fun createService() {
        var intent = Intent(self, VoiceCallService::class.java)
        bundleOf(
            Constant.USER to userRecipient, Constant.CHECK_CALL to isCheckCall
        ).also {
            intent.putExtra(Constant.USER, it)
        }
        startService(intent)
        serviceConnection?.let { bindService(intent, it, BIND_AUTO_CREATE) }
    }

    private fun stopService() {
        stopService(intent)
        serviceConnection?.let { unbindService(it) }
    }

    override fun onComing() {
    }

    override fun onEstablish() {
        createService()
        lblStatus.text = "Bắt đầu"
        layoutOpCalling.visible()
        layoutReceive.gone()
        imgCall.gone()
        cmtTime.visible()
        cmtTime.start()
    }

    override fun onCallEnd() {

        stopService()
        imgCall.visible()
        layoutOpCalling.gone()
        lblStatus.text = "Kết thúc cuộc gọi"
        cmtTime.stop()
    }

}