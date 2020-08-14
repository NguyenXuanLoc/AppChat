package com.example.appchat.ui.chat

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.login.LoginActivity
import com.example.fcm.common.ext.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : BaseActivity(), ChatView {
    private val presenter by lazy { ChatPresenter(this) }
    private var userReceiver: UserModel? = null
    private val messages by lazy { ArrayList<MessageModel>() }
    private val adapter by lazy {
        userReceiver?.let {
            getUser()?.let { it1 ->
                ChatAdapter(messages, self, it, it1) { itemClick(it) }
            }
        }
    }

    private var nodeChild = ""
    private var isLoading = true
    private var isBottom = false
    private var isLoadNew = true

    private var topNode: String? = null
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
        }
    }

    override fun eventHandle() {
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
            userReceiver?.let { it -> presenter.sentMessage(nodeChild, model, it) }
            model.isSend = Constant.SENDING
            if (isLoadNew) {
                presenter.loadNewMessage(nodeChild)
                isLoadNew = false
            }
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


    private fun itemClick(messageModel: MessageModel) {

    }

    private fun pagination(rcl: RecyclerView) {
        rcl.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val lastItem = linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                val firstItem = linearLayoutManager?.findFirstCompletelyVisibleItemPosition()

                // Check if scroll to bottom, it hava masage then auto scroll bottom
                if (lastItem == messages.size - 1 && linearLayoutManager != null) {
                    isBottom = true
                } else if (isBottom) {
                    isBottom = false
                }

                if (firstItem == 1 && linearLayoutManager != null && !isLoading
                ) {
                    messages.addAll(messages)
                    adapter?.notifyItemInserted(0)
                    /* adapter?.addItemLoading()
                     toast("top")
                     presenter.loadOldMessage(nodeChild, topNode.toString())
                     isLoading = true*/
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun loadMessageSuccess(list: ArrayList<MessageModel>, isCheck: Boolean) {
        if (list.size > 0) {
            topNode = list[0].id
            if (isCheck) {
                messages.addAll(list)
                adapter?.notifyDataSetChanged()
                rclChat.scrollToPosition(messages.size - 1)
                isLoading = false
                pagination(rclChat)
            } else {
                adapter?.removeItemLoading()
                messages.addAll(0, list)
                adapter?.notifyItemInserted(0)
                Log.e("TAG", "more--------")
                list.forEach { it ->
                    Log.e("TAG", it.message)
                }
                if (list.size > 0) isLoading = false
            }

        }
    }

    override fun loadNodeChildSuccess(node: String) {
        nodeChild = node
    }

    override fun nullNodeChild() {
    }

    override fun loadNewMessageSuccess(model: MessageModel) {
        messages.add(model)
        adapter?.notifyDataSetChanged()
        if (isBottom) {
            rclChat.scrollToPosition(messages.size - 1)
            isBottom = false
        }

    }

    override fun loadTokenSuccess(result: String) {
        token = result
    }

    override fun nullOldMessage() {
        adapter?.removeItemLoading()
    }

}