package com.example.appchat.ui.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.OptionModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.voicecall.VoiceCallPresenter
import com.example.appchat.ui.voicecall.VoiceCallView
import com.example.appchat.ui.voicecall.handleCall.VoiceCallService
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_voice_call.*
import java.util.*


class FragTest : BaseActivity(), VoiceCallView {
    private val presenter by lazy { VoiceCallPresenter(this) }
    private var userRecipient: UserModel? = null
    private var token: String? = null
    private var callerId: String? = null
    private var recipientId: String? = null
    private var isCheckCall: Boolean? = null
    private var service: VoiceCallService? = null
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
                val binder: VoiceCallService.VoiceCallBinder =
                    iBinder as VoiceCallService.VoiceCallBinder
                service = binder.getService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }
    }

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
    }

    override fun eventHandle() {
        createService()
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
            Log.e("TAG", "recipientId $recipientId")
            recipientId?.let { it1 -> service!!.call(it1) }
        }
        imgReceive.setOnClickListener {
            Log.e("TAG", "recipientId $recipientId")
            recipientId?.let { it1 -> service?.call(it1) }
        }
        imgDeject.setOnClickListener {
        }
        imgStop.setOnClickListener { }
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
            token = it.getString(Constant.TOKEN)
        }
        if (isCheckCall == true) {
            recipientId = "1"
            callerId = "2"
        } else if (isCheckCall == false) {
            recipientId = "2"
            callerId = "1"
        }
    }

    fun createService() {
        var intent = Intent(self, VoiceCallService::class.java)
        bundleOf(
            Constant.USER to userRecipient, Constant.CHECK_CALL to isCheckCall
        ).also {
            intent.putExtra(Constant.USER, it)
        }
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }
}