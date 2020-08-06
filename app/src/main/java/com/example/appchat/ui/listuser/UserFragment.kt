package com.example.appchat.ui.listuser

import android.view.Menu
import android.view.MenuInflater
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.chat.ChatActivity
import com.example.fcm.common.ext.openActivity
import kotlinx.android.synthetic.main.frag_chat_friend.view.*

@Suppress("DEPRECATION")
class UserFragment : BaseFragment(), UserFragmentView {
    private val users by lazy { ArrayList<UserModel>() }
    private val presenter by lazy { UserFragmentPresenter(this) }
    private val adapter by lazy { self?.let { ListUserAdapter(users, it) { it -> onClick(it) } } }

    private var isViewed = false

    companion object {
        fun newInstance(): UserFragment {
            return UserFragment()
        }
    }

    private var isLoading = true
    private var lastNode: String? = null
    private var firstNode: String? = null


    override fun onCreateView(): Int {
        return R.layout.frag_chat_friend
    }

    override fun init() {
        setHasOptionsMenu(true)
        mView.rclUser.adapter = adapter
        mView.rclUser.layoutManager = LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false)
        mView.rclUser.setHasFixedSize(true)
    }

    override fun eventHandle() {
        paginationScroll(mView.rclUser)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_status, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        if (!isViewed) {
            dialogLoading?.show()
            presenter.getFirstKey(Key.USER)
            //Get last key and load list user
            presenter.getLastKey(Key.USER)
            isViewed = true
        }
        changeNavigationIcon(R.drawable.ic_filter)
    }

    override fun resultLoadMoreList(list: ArrayList<UserModel>) {
        if (list.size > 0) {
            adapter?.removeLoading()
            for (i in list.size - 1 downTo 0) {
                users.add(list[i])
            }
            lastNode = users[users.size - 1].id.toString()
            isLoading = false
            adapter?.notifyDataSetChanged()
        }
    }

    override fun resultListUser(list: ArrayList<UserModel>) {
        dialogLoading?.dismiss()
        if (list.size > 0) {
            for (i in list.size - 1 downTo 0) {
                users.add(list[i])
            }
            lastNode = users[users.size - 1].id.toString()
            isLoading = false
            adapter?.notifyDataSetChanged()
        }
    }

    override fun resultFirstKey(firstKey: String) {
        firstNode = firstKey

    }

    override fun resultNull() {
        if (users.size > 0) adapter?.removeLoading()
    }

    private fun paginationScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val fistLastItem = linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                if (!isLoading && linearLayoutManager != null && fistLastItem == users.size - 1 && lastNode != firstNode
                ) {
                    adapter?.addLoading()
                    presenter.loadListUser(Key.USER, lastNode.toString(), true)
                    isLoading = true
                }
            }
        })
    }

    private fun onClick(model: UserModel) {
        bundleOf(Constant.MESSAGE to model).also {
            openActivity(ChatActivity::class.java, it, Constant.MESSAGE)
        }
    }
}