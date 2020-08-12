package com.example.appchat.ui.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appchat.ui.news.FollowingNews.FollowingNewsFragment
import com.example.appchat.ui.news.LatestNews.LatestNewFragment
import com.example.appchat.ui.news.SuggesstionNews.SuggestionNewFragment

class NewsPagerStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val pager by lazy {
        arrayListOf(
            LatestNewFragment.newInstance(), SuggestionNewFragment.newInstance(),
            FollowingNewsFragment.newInstance()
        )
    }

    override fun getItemCount(): Int {
        return pager.size
    }

    override fun createFragment(position: Int): Fragment {
        return pager[position]
    }
}