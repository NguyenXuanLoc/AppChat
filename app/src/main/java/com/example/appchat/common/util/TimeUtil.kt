package com.example.appchat.common.util

import android.content.Context
import com.example.appchat.R
import java.text.SimpleDateFormat
import java.util.*


object TimeUtil {
    fun getTimeUpload(date: String, time: String, ctx: Context): String {
        var result = ""
        val sdfDate = SimpleDateFormat("dd-MM")
        val currentDate: String = SimpleDateFormat("dd-MM", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        var startDate: Date = sdfDate.parse(date)
        var enDate: Date = sdfDate.parse(currentDate)
        if (startDate.time == enDate.time) {
            val sdfTime = SimpleDateFormat("HH:mm")
            var startTime = sdfTime.parse(time)
            var endTime = sdfTime.parse(currentTime)
            result = if (endTime == startTime) {
                ctx.getString(R.string.just_finished)
            } else {
                var duration = endTime.time - startTime.time
                var hours = (duration / (1000 * 60 * 60)).toInt()
                var min = duration / (1000 * 60)
                if (hours > 0) "$hours ${ctx.getString(R.string.our)}"
                else "$min ${ctx.getString(R.string.minute)}"

            }
        } else {
            var duration = enDate.time - startDate.time
            var days = (duration / (1000 * 60 * 60 * 24)).toInt()
            result = if (days <= 2) {
                "$days ${ctx.getString(R.string.day)}"
            } else {
                date
            }
        }
        return result
    }

    fun removeYear(date: String): String {
        var list = date.split("/")
        return "${list[0]},${list[1]}"
    }
}