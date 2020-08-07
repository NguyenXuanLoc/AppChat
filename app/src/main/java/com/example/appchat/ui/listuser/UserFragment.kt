package com.example.appchat.ui.listuser

import android.view.Menu
import android.view.MenuInflater
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.OptionModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.chat.ChatActivity
import com.example.appchat.widget.PaginationScrollNestedListener
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.frag_chat_friend.view.*


@Suppress("DEPRECATION")
class UserFragment : BaseFragment(), UserFragmentView {
    private val users by lazy { ArrayList<UserModel>() }
    private val presenter by lazy { UserFragmentPresenter(this) }
    private val adapter by lazy { self?.let { UserAdapter(users, it) { it -> onClick(it) } } }

    private var isViewed = false
    private var isLoading = true
    private var lastNode: String? = null
    private var firstNode: String? = null

    private val options by lazy { ArrayList<OptionModel>() }
    private val adapterOption by lazy {
        self?.let {
            OptionAdapter(
                options,
                it
            ) { it -> onClickOptions(it) }
        }
    }
    private val pagination by lazy {
        object : PaginationScrollNestedListener() {
            override fun loadMore() {
                adapter?.addLoading()
                presenter.loadListUser(Key.USER, lastNode.toString(), true)
                isLoading = true
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        }
    }

    companion object {
        fun newInstance(): UserFragment {
            return UserFragment()
        }
    }


    override fun onCreateView(): Int {
        return R.layout.frag_chat_friend
    }

    override fun init() {
        setHasOptionsMenu(true)
        mView.rclUser.adapter = adapter
        mView.rclUser.layoutManager = LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false)
        mView.rclUser.setHasFixedSize(true)
        pagination.setLayoutManager(mView.rclUser.layoutManager as LinearLayoutManager)

        mView.rclOptions.adapter = adapterOption
        mView.rclOptions.layoutManager =
            GridLayoutManager(self, 3, GridLayoutManager.VERTICAL, false)
        mView.rclOptions.setHasFixedSize(true)

    }

    override fun eventHandle() {
        mView.nestedScroll.setOnScrollChangeListener(pagination)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_status, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        if (!isViewed) {
            dialogLoading?.show()
            presenter.loadOptions()
            presenter.getFirstKey(Key.USER)
            //Get last key and load list user
            presenter.getLastKey(Key.USER)
            isViewed = true
        }
        changeNavigationIcon(R.drawable.ic_filter)
    }

    override fun resultLoadMoreList(list: ArrayList<UserModel>) {
        users.removeAt(users.size - 1)
        mView.rclUser.scrollToPosition(adapter!!.itemCount - 1)
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

    override fun resultOptions(list: ArrayList<OptionModel>) {
        options.addAll(list)
        adapterOption?.notifyDataSetChanged()
    }

    private fun onClick(model: UserModel) {
        bundleOf(Constant.MESSAGE to model).also {
            openActivity(ChatActivity::class.java, it, Constant.MESSAGE)
        }
    }

    private fun onClickOptions(model: OptionModel) {
        toast(model.action.toString())
    }
}