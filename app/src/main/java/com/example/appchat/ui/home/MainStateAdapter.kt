package com.example.appchat.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appchat.ui.listuser.UserFragment
import com.example.appchat.ui.news.NewsFragment
import com.example.appchat.ui.conversation.ConversationFragment
import com.example.appchat.ui.me.MeFragment


class MainStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val pages by lazy {
        arrayListOf(
            UserFragment.newInstance(),
            NewsFragment.newInstance(),
            ConversationFragment.newInstance(),
            MeFragment.newInstance()
        )
    }

    override fun getItemCount(): Int {
        return pages.size
    }
    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}