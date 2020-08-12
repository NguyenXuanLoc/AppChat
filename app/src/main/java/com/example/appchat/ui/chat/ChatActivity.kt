package com.example.appchat.ui.chat

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : BaseActivity(), ChatView {
    private val presenter by lazy { ChatPresenter(this) }
    private var userReceiver: UserModel? = null
    private var nodeChild = ""
    private val messages by lazy { ArrayList<MessageModel>() }
    private val adapter by lazy {
        userReceiver?.let {
            getUser()?.let { it1 ->
                ChatAdapter(messages, self, it, it1) { itemClick(it) }
            }
        }
    }
    private var isLoading = false
    private var lastNode: String? = null
    private var token: String? = null
    override fun contentView(): Int {
        return R.layout.activity_chat
    }

    override fun init() {
        hideToolbarBase()
        applyToolbar(toolbar, R.color.white)
        enableHomeAsUp(toolbar) { finish() }
        changeNavigationIcon()
        rclChat.layoutManager = LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false)
        rclChat.adapter = adapter
        rclChat.setHasFixedSize(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_message, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.MESSAGE)
        if (bundle != null) {
            userReceiver = bundle.getSerializable(Constant.MESSAGE) as UserModel?
            toast(userReceiver?.userName)
        }
    }

    override fun eventHandle() {
        pagination(rclChat)
        userReceiver?.let {
            lblNickName.text = it.userName
            lblStatus.text = it.status
            presenter.getTokenAndCheckStatus(it)
        }
        presenter.checkNodeChild(getUser()?.id.toString(), userReceiver?.id.toString())

        //Listener
        lblSend.setOnClickListener {
            var model = MessageModel(
                sender = getUser()?.id,
                received = userReceiver?.id,
                message = edtMessage.text.toString(),
                isSend = Constant.RECEIVED
            )
            if (nodeChild?.isEmpty()) nodeChild = model.sender + model.received
            userReceiver?.let { it1 -> presenter.sentMessage(nodeChild, model, it1) }
            model.isSend = Constant.SENDING
            messages.add(model)
            adapter?.notifyDataSetChanged()
            presenter.loadMore(nodeChild, lastNode.toString(), false)
            edtMessage.setText("")
        }
        edtMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    imgGift.visible()
                    lblStatus.gone()
                } else {
                    imgGift.gone()
                    lblSend.visible()
                }
            }
        })

    }

    override fun loadMoreSuccess(list: ArrayList<MessageModel>) {
        adapter?.removeItemLoading()
        if (list.size > 0) {
            isLoading = false
            messages.addAll(list)
            adapter?.notifyDataSetChanged()
            lastNode = messages[messages.size - 1].id
        } else {
            isLoading = true
        }
    }

    override fun loadMessageSuccess(list: ArrayList<MessageModel>) {
        if (list.size > 0) {
            messages.addAll(list)
            adapter?.notifyDataSetChanged()
            lastNode = messages[messages.size - 1].id
        }
    }

    override fun loadNodeChildSuccess(node: String) {
        nodeChild = node
    }

    override fun nullNodeChild() {
    }

    override fun loadNewMessageSuccess(list: ArrayList<MessageModel>) {
        if (list.size > 0) {
            if (messages.size > 0) {
                messages.removeAt(messages.size - 1)
            }
            lastNode = list[0].id
            messages.addAll(list)
            adapter?.notifyDataSetChanged()
            rclChat.scrollToPosition(messages.size - 1)
        }
    }

    override fun loadTokenSuccess(result: String) {
        token = result
    }

    private fun itemClick(messageModel: MessageModel) {

    }

    private fun pagination(rcl: RecyclerView) {
        rcl.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val lastItem = linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                if (lastItem == messages.size - 1 && linearLayoutManager != null && !isLoading) {
                    isLoading = true
                    adapter?.addItemLoading()
                    presenter.loadMore(nodeChild, lastNode.toString())
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
}