package com.example.appchat.common.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.exoplayer2.util.Log
import kotlinx.android.synthetic.main.activity_status.*

object MediaUtil {
    fun playAudio(mediaPlayer: MediaPlayer, uri: Uri, ctx: Context) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(ctx, uri)
        try {
            mediaPlayer.prepare()
        } catch (e: Exception) {
            Log.e("TAG", e.message.toString())
        }
        mediaPlayer.start()
    }

    fun stopAudio(mediaPlayer: MediaPlayer) {
        mediaPlayer.stop()
    }

    fun continueAudio(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }


}