package com.example.appchat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appchat.widget.DialogLoading
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.visible
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_base.*

@Suppress("DEPRECATION")
abstract class BaseFragment() : Fragment() {
    open lateinit var mView: View
    open val self by lazy { activity }
    open val dialogLoading by lazy { activity?.let { DialogLoading(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Fresco.initialize(activity)
        mView = inflater.inflate(onCreateView(), container, false)
        getExtras()
        init()
        eventHandle()
        return mView
    }

    abstract fun onCreateView(): Int
    abstract fun init()
    abstract fun eventHandle()
    open fun getExtras() {}
    protected fun changeNavigationIcon(icon: Int) {
        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.run {
            setHomeAsUpIndicator(icon)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    // Hide base toolbar
    protected fun hideToolbarBase(toolbar: Toolbar) {
        toolbar.gone()
    }

    // Show Logo
    protected fun showLogo(logo: ImageView) {
        logo.visible()
    }


    // Show base toolbar
    protected fun showToolbarBase(toolbar: Toolbar) {
        toolbarBase.visible()
    }

    // Using toolbar
    protected fun showTitle(title: Any? = null, toolbar: Toolbar) {
        // Set title
        when (title) {
            is CharSequence -> toolbar.title = title
            is String -> toolbar.title = title
            is Int -> toolbar.title = getString(title)
        }
    }

    protected fun applyToolbar(
        toolbar: Toolbar,
        background: Int? = null,
        removeElevation: Boolean = false
    ) {
        var self = activity as AppCompatActivity
        self.setSupportActionBar(toolbar)
        self.supportActionBar?.setDisplayShowTitleEnabled(false)
        background?.run {
            toolbar.setBackgroundColor(self.resources.getColor(this))
        }

        if (removeElevation) {
            toolbar.elevation = 0f
        }
        setHasOptionsMenu(true)
    }

}