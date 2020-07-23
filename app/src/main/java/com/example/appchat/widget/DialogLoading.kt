package com.example.appchat.widget

import android.app.Dialog
import android.content.Context
import com.example.appchat.R

class DialogLoading(ctx: Context) : Dialog(ctx) {
    init {
        this.setContentView(R.layout.layout_process_loading)
        this.setCanceledOnTouchOutside(false)
    }

}