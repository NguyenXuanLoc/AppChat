package com.example.appchat.common.util

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.example.appchat.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtil {
    fun TextView.countTime(check: Boolean = true, time: Long = 30000) {
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

    fun getTime(date: String, time: String, ctx: Context): String {
        var result = ""
        val currentDate: String = SimpleDateFormat("dd-MM", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        if (currentDate == date) {
            val simpleDateFormat = SimpleDateFormat("hh:mm")
            var time1 = simpleDateFormat.parse(time)
            var time2 = simpleDateFormat.parse(currentTime)
            if (time1 == time2) {
                result = ctx.getString(R.string.just_finished)
            } else {
                val difference: Long = time2.time - time1.time
                var days = (difference / (1000 * 60 * 60 * 24)).toInt()
                var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
                var min =
                    (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
                result = when {
                    days > 0 -> "$days ${ctx.getString(R.string.day)}"
                    hours > 0 -> "$hours ${ctx.getString(R.string.our)}"
                    else -> {
                        "$min ${ctx.getString(R.string.minute)}"
                    }
                }
            }
        } else {
            var dates: List<String> = date.split("-")
            var currentDates: List<String> = currentDate.split("-")
            if (dates[dates.size - 1] == currentDates[currentDates.size - 1]) {
                var time = currentDates[0].toInt() - dates[0].toInt()
                result = if (time <= 2) "$time ${ctx.getString(R.string.day)}"
                else date
            }
        }
        return result
    }
}