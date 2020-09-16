package com.example.appchat.ui.videocall

import android.view.View
import com.example.appchat.R
import com.example.appchat.ui.base.BaseActivity
import com.sinch.android.rtc.PushPair
import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.video.VideoCallListener
import com.sinch.android.rtc.video.VideoController


class VideoCallActivity : BaseActivity(), VideoCallListener {
    private val APP_KEY = "94bc354a-8d1e-4dd9-be8e-cc370ed1e448"
    private val APP_SECRET = "1Ce1WvTxp0u7mFNQXRt0Tw=="
    private val ENVIRONMENT = "clientapi.sinch.com"

    var call: Call? = null
    private lateinit var sinchClient: SinchClient
    var callerId: String? = null
    var recipientId: String? = null

    override fun contentView(): Int {
        return R.layout.activity_video_call
    }

    override fun init() {
    }

    override fun eventHandle() {
    }

    override fun onCallProgressing(p0: Call?) {

    }

    override fun onCallEstablished(p0: Call?) {

    }

    override fun onCallEnded(p0: Call?) {
        call?.hangup()
    }

    override fun onShouldSendPushNotification(p0: Call?, p1: MutableList<PushPair>?) {
    }

    override fun onVideoTrackAdded(p0: Call?) {
        val vc: VideoController = sinchClient.videoController
        val myPreview: View = vc.localView
        val remoteView: View = vc.remoteView
    }

    override fun onVideoTrackPaused(p0: Call?) {
        call?.pauseVideo()
    }

    override fun onVideoTrackResumed(p0: Call?) {

    }
}