package com.example.appchat.ui.test

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log
import com.example.appchat.R
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.toast
import java.io.IOException


class test : BaseActivity() {
    override fun contentView(): Int {
        return R.layout.activity_register
    }


    override fun init() {
        toast("OK")
        val path = "http://dleelbaha.com/fayziah/download/sound/1536950612.mp3"
        val mp = MediaPlayer()
        try {
            mp.reset()
            mp.setDataSource(path)
            mp.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mp.setOnPreparedListener {
            var duration = mp.duration
            if (duration <= 0) {
                duration = getDurationInMilliseconds(path)
            }
            toast("$duration ms")
            Log.i("time", "$duration ms")
        }
    }

    private fun getDurationInMilliseconds(path: String): Int {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        val duration: Int =
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
        mmr.release()
        return duration
    }

    override fun eventHandle() {

    }
}