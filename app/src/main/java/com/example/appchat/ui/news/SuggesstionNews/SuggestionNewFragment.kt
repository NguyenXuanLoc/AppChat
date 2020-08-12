package com.example.appchat.ui.news.SuggesstionNews

import com.example.appchat.R
import com.example.appchat.ui.base.BaseFragment

class SuggestionNewFragment : BaseFragment() {
    companion object {
        fun newInstance(): SuggestionNewFragment {
            return SuggestionNewFragment()
        }
    }

    override fun onCreateView(): Int {
        return R.layout.fragment_conversation
    }

    override fun init() {
    }

    override fun eventHandle() {
    }
}