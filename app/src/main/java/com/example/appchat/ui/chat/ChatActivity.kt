package com.example.appchat.ui.chat

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImage
import com.example.appchat.data.model.GifModel
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.chat.gif.GifAdapter
import com.example.appchat.widget.PaginationScrollListener
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import kotlinx.android.synthetic.main.activity_chat.*

@Suppress("DEPRECATION")
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

    // top node message to new old message
    private var topNode: String? = null
    private var token: String? = null

    //Load gif
    private var isLoadingGif = true
    private val gifs by lazy { ArrayList<GifModel>() }
    private val adapterGif by lazy { GifAdapter(gifs, self) { it -> itemClickGif(it) } }
    private val pagination by lazy {
        object : PaginationScrollListener(rclGif.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                var lastNode = gifs[gifs.size - 1].id.toString()
                isLoadingGif = true
                adapterGif.addLoading()
                presenter.loadMoreGif(lastNode)
            }

            override fun isLoading(): Boolean {
                return isLoadingGif
            }

        }
    }

    // is option = true -> send message , is option == false send Like
    private var isOption = true

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

        //gif
        rclGif.layoutManager = LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false)
        rclGif.adapter = adapterGif
        rclGif.setHasFixedSize(true)
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
        rclGif.setOnScrollListener(pagination)
        //Listener
        /*  lblSend.setOnClickListener {
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
          }*/
        edtMessage.setOnClickListener {
            imgShow.visible()
            goneItemOption()
        }
        edtMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rclGif.gone()
                if (s.isNullOrEmpty()) {
                    imgOption.setImage(R.drawable.btn_like, self)
                    isOption = false
                    imgGif.visible()
                } else {
                    imgOption.setImage(R.drawable.btn_send, self)
                    isOption = true
                    imgGif.gone()
                }
            }
        })
        imgGif.setOnClickListener {
            presenter.loadGif()
        }
        imgShow.setOnClickListener {
            showItemOption()
        }
        imgOption.setOnClickListener {
            var model = MessageModel(
                sender = getUser()?.id,
                received = userReceiver?.id,
                isSend = Constant.RECEIVED
            )
            if (nodeChild?.isEmpty()) nodeChild = model.sender + model.received
            if (isLoadNew) {
                presenter.loadNewMessage(nodeChild)
                isLoadNew = false
            }

            if (isOption) {
                model.message = edtMessage.text.toString()
                edtMessage.setText("")
            } else {
                model.like = Key.LIKE
            }
            userReceiver?.let { it -> presenter.sentMessage(nodeChild, model, it) }
        }
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
                    presenter.loadOldMessage(nodeChild, topNode.toString())
                    isLoading = true
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
                messages.addAll(0, list)
                adapter?.notifyItemInserted(0)
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
    }

    private fun itemClickGif(model: GifModel) {
        var messageModel = MessageModel(
            sender = getUser()?.id,
            received = userReceiver?.id,
            isSend = Constant.RECEIVED,
            urlGif = model.url
        )
        userReceiver?.let { it -> presenter.sentMessage(nodeChild, messageModel, it) }
        rclGif.gone()
    }

    override fun loadGifSuccess(list: ArrayList<GifModel>) {
        gifs.addAll(list)
        rclGif.visible()
        adapterGif.notifyDataSetChanged()
        isLoadingGif = false
    }

    override fun loadMoreGifSuccess(list: ArrayList<GifModel>) {
        adapterGif.removeLoading()
        if (list.size > 0) {
            gifs.addAll(list)
            adapterGif.notifyItemInserted(gifs.size - 1)
            isLoadingGif = false
        }

    }

    private fun goneItemOption() {
        imgCamera.gone()
        imgVoice.gone()
        imgChooseImg.gone()
    }

    private fun showItemOption() {
        imgCamera.visible()
        imgVoice.visible()
        imgChooseImg.visible()
    }
}