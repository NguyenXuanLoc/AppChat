package com.example.appchat.widget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.ext.setImage
import com.example.appchat.common.util.PermissionUtil
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_audio.*
import java.io.File


@Suppress("DEPRECATION")
class DialogAudio(var ctx: Context) : BottomSheetDialog(ctx, R.style.BottomSheepDialogTheme) {
    private var listener: AudioListener? = null
    private lateinit var mRecorder: MediaRecorder
    private val mPlayer by lazy { MediaPlayer() }
    private var mFileName: String? = null
    private var checkStatus = 0
    private lateinit var countDownTimer: CountDownTimer
    private var time: Long = 0
    private val self by lazy { ctx as Activity }

    init {
        setContentView(R.layout.layout_audio)
        mFileName = Environment.getExternalStorageDirectory().absolutePath;
        mFileName += Constant.FILE_NAME
        imgRecord.setOnClickListener {
            if (PermissionUtil.isGranted(self, arrayOf(Manifest.permission.RECORD_AUDIO), 11, true)
            ) {
                checkStatus()
            }

        }
        imgDelete.setOnClickListener {
            checkStatus = 0
            imgRecord.setImage(R.drawable.ic_microphone_white, self)
            lblTime.setText(R.string.click_to_record)
            mRecorder.reset()
            mPlayer.reset()
            showHide(imgDelete, imgApply, false)
        }
        imgApply.setOnClickListener {
            listener?.onClickAudio(Uri.fromFile(File(mFileName)), getDuration())
            this.dismiss()
        }
    }

    private fun initRecord() {
        mRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(mFileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        }
    }

    private fun initMedia() {
        mPlayer.reset()
        mPlayer?.setDataSource(mFileName)
        mPlayer?.prepare()
        mPlayer?.start();
    }

    private fun getDuration(): String {
        mPlayer.reset()
        mPlayer?.setDataSource(mFileName)
        mPlayer?.prepare()
        return mPlayer.duration.toString()
    }

    fun setAudioListener(listener: AudioListener) {
        this.listener = listener
    }

    interface AudioListener {
        fun onClickAudio(uri: Uri, time: String)
    }

    //Time record check == true: count up, check == false count down
    private fun countTime(lblTime: TextView, check: Boolean = true, time: Long = 30000) {
        countDownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var time = if (check) (time / 1000 - millisUntilFinished / 1000)
                else millisUntilFinished / 1000
                lblTime.text = "$time"
            }

            override fun onFinish() {
                checkStatus = 2
                lblTime.text = (time / 1000).toString()
                imgRecord.setImage(R.drawable.ic_play_white, ctx as Activity)
            }
        }.start()

    }

    //ShowHide Button
    private fun showHide(img1: ImageView, img2: ImageView, check: Boolean = true) {
        if (check) {
            img1.visible()
            img2.visible()
        } else {
            img1.gone()
            img2.gone()
        }
    }

    //Check status when user click:
    private fun checkStatus() {
        when (checkStatus) {
            0 -> {
                imgRecord.setImage(R.drawable.ic_stop_white, self)
                initRecord()
                try {
                    mRecorder.prepare()
                } catch (e: Exception) {
                    e.message
                }
                mRecorder.start()
                countTime(lblTime)
                checkStatus++
            }
            1 -> {
                imgRecord.setImage(R.drawable.ic_play_white, self)
                mRecorder.stop()

                countDownTimer.cancel()
                showHide(imgApply, imgDelete)
                checkStatus++
            }
            2 -> {
                initMedia()
                time = (lblTime.text.toString().toInt() * 1000).toLong()
                imgRecord.setImage(R.drawable.ic_stop_white, self)
                countTime(lblTime, false, time)

                checkStatus++
            }
            3 -> {
                imgRecord.setImage(R.drawable.ic_play_white, self)
                mPlayer.reset()
                countDownTimer.cancel()

                checkStatus--
            }
        }
    }
}