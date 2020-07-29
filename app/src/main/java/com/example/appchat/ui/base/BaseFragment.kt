package com.example.appchat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco

abstract class BaseFragment() : Fragment() {
    open lateinit var mView: View
    open val self by lazy { activity }
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
}