package com.example.appchat.ui.home

import android.view.View
import androidx.fragment.app.*
import androidx.viewpager.widget.PagerAdapter
import com.example.appchat.ui.chatfriend.FragFriend
import com.example.appchat.ui.chatgroup.FragGroup
import com.example.appchat.ui.notification.FragNotification
import com.example.appchat.ui.personal.FragPersonal


class MainStateAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return FragFriend()
            1 -> return FragGroup()
            2 -> return FragNotification()
            else -> return FragPersonal()
        }
    }

    override fun getCount(): Int {
        return 4
    }

}