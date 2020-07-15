package com.example.appchat.ui.chatfriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appchat.R
import com.example.fcm.common.ext.toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.frag_chat_friend.view.*

class FragFriend : Fragment() {

    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.frag_chat_friend, container, false)
        init()
        return mView
    }

    private fun init() {
    }
}