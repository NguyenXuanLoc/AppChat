package com.example.appchat.common.util

import android.os.CountDownTimer
import android.widget.TextView
import com.example.appchat.common.ext.countTime
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun TextView.countTime(
        check: Boolean = true,
        time: Long = 30000,
        countDownTimer: CountDownTimer? = null
    ) {
        this.text = (time / 1000).toString()
        object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var time = if (check) (time / 1000 - millisUntilFinished / 1000)
                else millisUntilFinished / 1000
                this@countTime.text = "$time"
            }

            override fun onFinish() {
                this@countTime.text = (time / 1000).toString()
            }
        }.start()

    }

    fun checkTime(time: String, date: String) {
        val time = Calendar.getInstance().time
      /*  val currentDate: String = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val date = Date(currentDate)*/
    }
}