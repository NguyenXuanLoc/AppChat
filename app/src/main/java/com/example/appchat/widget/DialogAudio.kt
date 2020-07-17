package com.example.appchat.widget

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
import com.example.appchat.common.ext.setImage
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_audio.*
import java.io.File
import kotlin.concurrent.timer


class DialogAudio(ctx: Context) : BottomSheetDialog(ctx, R.style.BottomSheepDialogTheme) {
    private var listener: AudioListener? = null
    private lateinit var mRecorder: MediaRecorder
    private val mPlayer by lazy { MediaPlayer() }
    private var mFileName: String? = null
    private var checkStatus = 0
    private lateinit var countDownTimer: CountDownTimer
    private var time: Long = 0

    init {
        setContentView(R.layout.layout_audio)
        mFileName = Environment.getExternalStorageDirectory().absolutePath;
        mFileName += "/recorded_audio.3gp";
        imgRecord.setOnClickListener {
            when (checkStatus) {
                0 -> {
                    imgRecord.setImage(R.drawable.ic_stop_white)
                    initRecord()
                    try {
                        mRecorder.prepare()
                    } catch (e: Exception) {
                        Log.e("TAG", e.message.toString())
                    }
                    mRecorder.start()
                    countTime(lblTime)
                    checkStatus++
                }
                1 -> {
                    imgRecord.setImage(R.drawable.ic_play_white)
                    mRecorder.stop()
                    mPlayer?.setDataSource(mFileName);
                    mPlayer?.prepare();
                    time = (lblTime.text.toString().toInt() * 1000).toLong()
                    countDownTimer.cancel()
                    showHide(imgApply, imgDelete)
                    checkStatus++
                }
                2 -> {
                    imgRecord.setImage(R.drawable.ic_stop_white)
                    countTime(lblTime, false, time)
                    mPlayer?.start();
                    checkStatus++
                }
                3 -> {
                    imgRecord.setImage(R.drawable.ic_play_white)
                    mPlayer.reset()
                    countDownTimer.cancel()
                    checkStatus--
                }
            }
        }
        imgDelete.setOnClickListener {
            checkStatus = 0
            imgRecord.setImage(R.drawable.ic_microphone_white)
            lblTime.setText(R.string.click_to_record)
            mRecorder.reset()
            mPlayer.reset()
            showHide(imgDelete, imgApply, false)
        }
        imgApply.setOnClickListener {
            var uri = Uri.fromFile(File(mFileName))
            listener?.onClickApply(uri)
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

    fun setAudioListener(listener: AudioListener) {
        this.listener = listener
    }

    interface AudioListener {
        fun onClickApply(uri: Uri)
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
                imgRecord.setImage(R.drawable.ic_play_white)
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
}