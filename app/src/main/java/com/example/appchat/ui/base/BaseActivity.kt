package com.example.appchat.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.appchat.R
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.facebook.drawee.backends.pipeline.Fresco
import contentView
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity() {
    open val self by lazy { this }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        contentView()?.run {
            setContentView(R.layout.activity_base)
            layoutInflater.inflate(this, frlBase)
        }
        getExtra()
        init()
        eventHandle()
    }

    abstract fun contentView(): Int
    abstract fun init()
    abstract fun eventHandle()
    open fun getExtra() {}

    // Hide base toolbar
    protected fun hideToolbarBase() {
        toolbarBase.gone()
    }

    // Show Logo
    protected fun showLogo() {
        imgLogo.visible()
    }

    // Hide Logo
    protected fun hideLogo() {
        imgLogo.gone()
    }

    // Show base toolbar
    protected fun showToolbarBase() {
        toolbarBase.visible()
    }

    // Using toolbar
    protected fun showTitle(title: Any? = null) {
        // Set title
        when (title) {
            is CharSequence -> toolbarBase.title = title
            is String -> toolbarBase.title = title
            is Int -> toolbarBase.title = getString(title)
        }
    }

    protected fun applyToolbar(
        toolbar: Toolbar = toolbarBase,
        background: Int? = null,
        removeElevation: Boolean = false
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        background?.run {
            toolbar.setBackgroundColor(ContextCompat.getColor(self, background))
        }

        if (removeElevation) {
            toolbar.elevation = 0f
        }
    }

    //Change Icon
    protected fun changeNavigationIcon(icon: Int = R.drawable.ic_back_grey) {
        val actionBar = (self as AppCompatActivity?)?.supportActionBar
        actionBar?.run {
            setHomeAsUpIndicator(icon)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    // Show Back icon
    protected fun enableHomeAsUp(
        toolbar: Toolbar = toolbarBase,
        backArrowColorResId: Int? = null,
        up: () -> Unit
    ) {
        toolbar.run {
            navigationIcon = backArrowColorResId?.run {
                DrawerArrowDrawable(self).apply {
                    progress = 1f
                    color = ContextCompat.getColor(self, backArrowColorResId)
                }
            } ?: DrawerArrowDrawable(self).apply { progress = 1f }
            setNavigationOnClickListener { up() }
        }
    }
}