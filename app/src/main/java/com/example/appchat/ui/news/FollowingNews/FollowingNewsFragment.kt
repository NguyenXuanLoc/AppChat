package com.example.appchat.ui.news.FollowingNews

import com.example.appchat.R
import com.example.appchat.ui.base.BaseFragment

class FollowingNewsFragment : BaseFragment() {
    companion object {
        fun newInstance(): FollowingNewsFragment {
            return FollowingNewsFragment()
        }
    }

    override fun onCreateView(): Int {
        return R.layout.fragment_following_news
    }

    override fun init() {
    }

    override fun eventHandle() {
    }
}