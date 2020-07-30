package com.example.appchat.ui.playvideo

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.ext.setImage
import com.example.appchat.common.util.RealPathUtils
import com.example.appchat.data.model.VideoModel
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_play_video.*
import kotlinx.android.synthetic.main.activity_play_video.pbLoading
import kotlinx.android.synthetic.main.activity_play_video.spvVideo
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.layout_controller.*
import timber.log.Timber


@Suppress("DEPRECATION")
class PlayVideoActivity : BaseActivity(), Player.EventListener {
    private var uri: String? = null
    private var isCheckUri = true
    var isFull = true
    private lateinit var simpleExoplayer: SimpleExoPlayer
    var mediaSource: MediaSource? = null

    //Lấy độ dài video
    private
    val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }

    //bộ điều hướng mặc định
    private val trackSelector by lazy {
        DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            pbLoading.visible()
        } else if (playbackState == Player.STATE_READY) {
            pbLoading.gone()
        }
    }


    override fun contentView(): Int {
        return R.layout.activity_play_video
    }


    override fun init() {

    }

    override fun eventHandle() {
        imgApply.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(Constant.URI, uri)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        imgFullScreen.setOnClickListener {
            if (isFull) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                isFull = false
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                isFull = true
            }
        }
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.URI)
        if (bundle != null) {
            isCheckUri = false
            uri = bundle.getString(Constant.URI).toString()
        } else {
            isCheckUri = true
            bundle = intent.getBundleExtra(Constant.PLAY_VIDEO)
            uri = bundle.getString(Constant.PLAY_VIDEO).toString()
            uri?.let {
                imgApply.gone()
            }
        }
    }

    private fun initializeExoplayer() {
        var loadControl = DefaultLoadControl()
        simpleExoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
        mediaSource = if (isCheckUri) {
            buildMediaSourceOnline(Uri.parse(uri))
        } else {
            buildMediaSourceLocal(Uri.parse(uri))
        }
        simpleExoplayer.prepare(mediaSource!!)
        spvVideo.player = simpleExoplayer
        spvVideo.keepScreenOn = true
        simpleExoplayer.addListener(this)
        simpleExoplayer.playWhenReady = true
    }

    private fun buildMediaSourceLocal(uri: Uri): MediaSource? {
        return ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(this, "ua"),
            DefaultExtractorsFactory(), null, null
        )
    }

    private fun releaseExoplayer() {
        simpleExoplayer.release()
    }

    private fun prepareExoplayer() {
        val uri = Uri.parse(uri)
        val mediaSource = buildMediaSourceOnline(uri)
        simpleExoplayer.prepare(mediaSource)
    }

    private fun buildMediaSourceOnline(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")
        var extractor = DefaultExtractorsFactory()
        return ExtractorMediaSource(uri, dataSourceFactory, extractor, null, null)
    }

    override fun onPause() {
        super.onPause()
        releaseExoplayer()
    }

    override fun onStart() {
        super.onStart()
        initializeExoplayer()
    }
}