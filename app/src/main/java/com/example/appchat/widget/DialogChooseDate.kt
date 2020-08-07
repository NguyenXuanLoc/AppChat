package com.example.appchat.widget

import android.content.Context
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import com.example.appchat.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.layout_choose_date.*

class DialogChooseDate(var ctx: Context) : BottomSheetDialog(ctx, R.style.BottomSheepDialogTheme) {
    var day: String? = null
    var month: String? = null
    var year: String? = null
    var listener: DateChooseListener? = null

    init {
        this.setContentView(R.layout.layout_choose_date)
        nbDay.maxValue = 31
        nbDay.minValue = 1

        nbMonth.maxValue = 12
        nbMonth.minValue = 1

        nbYear.minValue = 1932
        nbYear.maxValue = 22222
        eventHandle()
    }

    fun eventHandle() {
        nbYear.setFormatter { value ->
            year = value.toString()
            value.toString()
        }
        nbMonth.setFormatter { value ->
            month = value.toString()
            value.toString()
        }
        nbDay.setFormatter { value ->
            day = value.toString()
            value.toString()
        }
        lblCancel.setOnClickListener { dismiss() }
        lblFinish.setOnClickListener {
            var date = "$day/$month/$year"
            if (date.isNotEmpty()) {
                listener?.resultDate("$day/$month/$year")
                dismiss()
            }
        }
    }

    fun setDateChooseListener(listener: DateChooseListener) {
        this.listener = listener
    }

    interface DateChooseListener {
        fun resultDate(birthDay: String)
    }
}