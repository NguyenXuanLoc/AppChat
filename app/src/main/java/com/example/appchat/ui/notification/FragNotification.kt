package com.example.appchat.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appchat.R

class FragNotification : Fragment() {
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.frag_notification, container, false)
        return mView
    }
}