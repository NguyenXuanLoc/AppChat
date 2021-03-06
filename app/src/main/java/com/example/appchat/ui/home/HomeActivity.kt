package com.example.appchat.ui.home

import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.appchat.R
import com.example.appchat.R.color.white
import com.example.appchat.common.Key
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.fcm.Token
import com.example.fcm.common.ext.getUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home.*

private const val HOME_INDEX = 0
private const val GROUP_INDEX = 1
private const val NOTIFY_INDEX = 2
private const val PERSONAL_INDEX = 3

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    HomeActivityView {
    private val presenter by lazy { HomeActivityPresenter(this) }
    override fun contentView(): Int {
        return R.layout.activity_home
    }

    override
    fun init() {
        hideToolbarBase()
        applyToolbar(background = white)
        bnvOptions.itemIconTintList = null
        bnvOptions.setOnNavigationItemSelectedListener(this)

        pagerOptions.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pagerOptions.isUserInputEnabled = false
        pagerOptions.adapter = MainStateAdapter(self)
        //Load one page
        pagerOptions.offsetLeftAndRight(1)
    }

    override fun eventHandle() {
        getExtra()
        updateToken()
    }

    override fun onStart() {
        super.onStart()
        presenter.updateStatus(getUser()?.id.toString(), Key.ONLINE)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.updateStatus(getUser()?.id.toString(), Key.OFFLINE)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        p0.isChecked = true
        when (p0.itemId) {
            R.id.nav_friend -> {
                pagerOptions.currentItem = HOME_INDEX
                return true
            }
            R.id.nav_group -> {
                pagerOptions.currentItem = GROUP_INDEX
                return true
            }
            R.id.nav_notification -> {
                pagerOptions.currentItem = NOTIFY_INDEX
                return true
            }
            R.id.nav_personal -> {
                pagerOptions.currentItem = PERSONAL_INDEX
                return true
            }
        }
        return false
    }

    private fun updateToken() {
        val refreshToken = FirebaseInstanceId.getInstance().token
        val token = refreshToken?.let { Token(it) }
        FirebaseDatabase.getInstance().getReference(Key.TOKENS)
            .child(getUser()?.id.toString()).setValue(token)
    }
}