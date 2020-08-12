package com.example.appchat.ui.news.LatestNews

import com.example.appchat.R
import com.example.appchat.ui.base.BaseFragment

class LatestNewFragment : BaseFragment() {
    companion object {
        fun newInstance(): LatestNewFragment {
            return LatestNewFragment()
        }
    }

    override fun onCreateView(): Int {
        return R.layout.fragment_latest_news
    }

    override fun init() {
    }

    override fun eventHandle() {
    }
}