package com.example.appchat.ui.conversation

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.chat.ChatActivity
import com.example.appchat.ui.conversation.conversationadapter.ConversationAdapter
import com.example.appchat.ui.uploadstatus.UploadStatusActivity
import com.example.appchat.widget.PaginationScrollNestedListener
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.openActivity
import kotlinx.android.synthetic.main.fragment_conversation.view.*

class ConversationFragment : BaseFragment(), ConversationFragmentView {
    private val users by lazy { ArrayList<UserModel>() }
    private val adapter by lazy {
        self?.let {
            self!!.getUser()?.let { it1 ->
                ConversationAdapter(
                    it1,
                    users,
                    it
                ) { itemClick(it) }
            }
        }
    }
    private val presenter by lazy { ConversationFragmentPresenter(this) }
    private var isLoading = true
    private var isViewed = true
    private val pagination by lazy {
        object :
            PaginationScrollNestedListener(mView.rclFriend.layoutManager as LinearLayoutManager?) {
            override fun loadMore() {
                isLoading = true
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        }
    }

    companion object {
        fun newInstance(): ConversationFragment {
            return ConversationFragment()
        }
    }

    override fun onCreateView(): Int {
        return R.layout.fragment_conversation
    }

    override fun init() {
        applyToolbar(mView.toolbar, removeElevation = true)
        mView.rclFriend.adapter = adapter
        mView.rclFriend.layoutManager =
            LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false)
        mView.rclFriend.setHasFixedSize(true)
    }

    override fun eventHandle() {
        mView.nestedScroll.setOnScrollChangeListener(pagination)
    }

    override fun onResume() {
        super.onResume()
        if (isViewed) {
            dialogLoading?.show()
            presenter.getIdFriend(self?.getUser()?.id.toString())
            isViewed = false
        }
    }

    private fun itemClick(userModel: UserModel) {
        bundleOf(Constant.MESSAGE to userModel).also {
            openActivity(ChatActivity::class.java, it, Constant.MESSAGE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_conversation, menu)
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

    override fun loadFriendSuccess(list: ArrayList<UserModel>) {
        if (list.size > 0) {
            dialogLoading?.dismiss()
            users.addAll(list)
            adapter?.notifyDataSetChanged()
            isLoading = false
        }
    }

    override fun nullResult() {
        dialogLoading?.dismiss()
    }
}