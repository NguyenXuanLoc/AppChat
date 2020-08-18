package com.example.appchat.ui.news

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.appchat.R
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.uploadstatus.UploadStatusActivity
import com.example.fcm.common.ext.openActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_base.view.*
import kotlinx.android.synthetic.main.fragment_news.view.*

@Suppress("DEPRECATION")
class NewsFragment : BaseFragment(), NewsFragmentView {
    private val presenter by lazy { NewsFragmentPresenter(this) }

    companion object {
        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }

    override fun onCreateView(): Int {
        return R.layout.fragment_news
    }

    override fun init() {
        applyToolbar(mView.toolbar)
        self?.let {
            mView.pagerNews.adapter = NewsPagerStateAdapter(self!!)
            mView.offsetLeftAndRight(1)
            mView.isNestedScrollingEnabled = true
        }
    }

    override fun eventHandle() {
        TabLayoutMediator(mView.tabNews, mView.pagerNews,
            TabLayoutMediator.OnConfigureTabCallback { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getText(R.string.latest)
                    }
                    1 -> {
                        tab.text = getText(R.string.suggestions)
                    }
                    2 -> {
                        tab.text = getText(R.string.following)
                    }
                }
            }).attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_news_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_write_status -> {
                openActivity(UploadStatusActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}