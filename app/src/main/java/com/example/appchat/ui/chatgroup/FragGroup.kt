package com.example.appchat.ui.chatgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appchat.R

class FragGroup : Fragment() {

    companion object {
        fun newInstance(): FragGroup {
            return FragGroup()
        }
    }

    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.frag_chat_group, container, false)
        return mView
    }
}