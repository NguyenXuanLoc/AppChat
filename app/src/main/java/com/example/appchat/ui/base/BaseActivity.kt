package com.example.appchat.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco

abstract class BaseActivity : AppCompatActivity() {
    open val self by lazy { this }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(contentView())
        getExtra()
        init()
        eventHandle()
    }

    abstract fun contentView(): Int
    abstract fun init()
    abstract fun eventHandle()
    open fun getExtra() {}
}