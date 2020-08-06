package com.example.appchat.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appchat.ui.listuser.UserFragment
import com.example.appchat.ui.chatgroup.FragGroup
import com.example.appchat.ui.notification.FragNotification
import com.example.appchat.ui.personal.PersonalFragment


class MainStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val pages by lazy {
        arrayListOf(
            UserFragment.newInstance(),
            FragGroup.newInstance(),
            FragNotification.newInstance(),
            PersonalFragment.newInstance()
        )
    }

    override fun getItemCount(): Int {
        return pages.size
    }
    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}