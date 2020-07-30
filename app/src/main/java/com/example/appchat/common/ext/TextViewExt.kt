package com.example.appchat.common.ext

import android.os.CountDownTimer
import android.widget.TextView


// Count Down Time check== true count down, check == false count up
fun TextView.countTime(
    check: Boolean = true,
    time: Long = 30000
) {

    this.text = (time / 1000).toString()
    object : CountDownTimer(time, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var time = if (check) (time / 1000 - millisUntilFinished / 1000)
            else millisUntilFinished / 1000
            this@countTime.text = "$time''"
        }

        override fun onFinish() {
            this@countTime.text = "${(time / 1000)} ''"
        }
    }.start()

}
