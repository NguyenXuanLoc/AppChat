package com.example.appchat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appchat.widget.DialogLoading
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_base.*

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


    abstract fun eventHandle()
    abstract fun init()
    abstract fun onCreateView(): Int
    open fun getExtras() {}
    protected fun changeNavigationIcon(icon: Int) {
        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.run {
            setHomeAsUpIndicator(icon)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}