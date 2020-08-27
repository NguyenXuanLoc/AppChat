package com.example.appchat.ui.chat

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImage
import com.example.appchat.data.model.GifModel
import com.example.appchat.data.model.MessageModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.chat.ChatAdapter.ChatAdapter
import com.example.appchat.ui.chat.gif.GifAdapter
import com.example.appchat.ui.test.TestActivty
import com.example.appchat.ui.voicecall.VoiceCallActivity
import com.example.appchat.widget.DialogAudio
import com.example.appchat.widget.LinearManager
import com.example.appchat.widget.sendphoto.DialogSendPhoto
import com.example.appchat.widget.PaginationScrollListener
import com.example.fcm.common.ext.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.layout_option_call.*

@Suppress("DEPRECATION")
class ChatActivity : BaseActivity(), ChatView, DialogSendPhoto.SendPhotoListener,
    DialogAudio.AudioListener {
    private val presenter by lazy { ChatPresenter(this) }
    private var userReceiver: UserModel? = null
    private val messages by lazy { ArrayList<MessageModel>() }
    private val adapter by lazy {
        userReceiver?.let {
            getUser()?.let { it1 ->
                ChatAdapter(
                    messages,
                    self,
                    it,
                    it1
                ) { itemClick(it) }
            }
        }
    }


    private var nodeChild = ""
    private var isLoading = true
    private var isBottom = false
    private var isLoadNew = true
    private var isNull = true
    private var isShowCall = true

    // top node message to new old message
    private var topNode: String? = null
    private var token: String? = null

    //Load gif
    private var isLoadingGif = true
    private val gifs by lazy { ArrayList<GifModel>() }
    private val adapterGif by lazy { GifAdapter(gifs, self) { it -> itemClickGif(it) } }
    private val paginationGif by lazy {
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
    private var isOption = false

    // Send Image
    private val dialogSendImage by lazy {
        DialogSendPhoto(
            self,
            userReceiver?.userName.toString()
        )
    }

    // Send Audio
    private val dialogSendAudio by lazy {
        DialogAudio(self)
    }

    override fun contentView(): Int {
        return R.layout.activity_chat
    }

    override fun init() {
        hideToolbarBase()
        applyToolbar(toolbar, R.color.white, false)
        enableHomeAsUp(toolbar) { finish() }
        changeNavigationIcon()
        rclChat.layoutManager = LinearManager(self, LinearLayoutManager.VERTICAL)
        rclChat.adapter = adapter
        rclChat.setHasFixedSize(true)

        //gif
        rclGif.layoutManager = LinearManager(self, LinearLayoutManager.HORIZONTAL)
        rclGif.adapter = adapterGif
        rclGif.setHasFixedSize(true)

        dialogSendImage.setSendPhotoListener(this)
        dialogSendAudio.setAudioListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_message, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.MESSAGE)
        bundle?.run {
            userReceiver = getSerializable(Constant.MESSAGE) as UserModel?
        }
    }

    override fun eventHandle() {
        userReceiver?.let {
            lblNickName.text = it.userName
            lblStatus.text = it.status
            presenter.getTokenAndCheckStatus(it)
        }
        presenter.checkNodeChild(getUser()?.id.toString(), userReceiver?.id.toString())
        rclGif.setOnScrollListener(paginationGif)
        //Listener
        edtMessage.setOnClickListener {
            imgShow.visible()
            goneItemOption()
        }
        edtMessage.setOnFocusChangeListener { _, _ ->
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
            if (gifs.size <= 0) {
                presenter.loadGif()
            }
            rclGif.visible()
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
            if (isOption) {
                model.message = edtMessage.text.toString()
                edtMessage.setText("")
            } else {
                model.like = Key.LIKE
            }
            if (userReceiver != null && nodeChild.isNotEmpty()) {
                presenter.sentMessage(
                    nodeChild,
                    model,
                    userReceiver!!
                )
            }
        }

        imgChooseImg.setOnClickListener { dialogSendImage.show() }
        imgRecordAudio.setOnClickListener { dialogSendAudio.show() }

        layoutCallSound.setOnClickListener {
            getUser()?.run {
                presenter.pushNotifyVoiceCall(
                    token.toString(), userName.toString(),
                    getString(R.string.call_from), id.toString()
                )
            }
            bundleOf(
                Constant.USER to userReceiver, Constant.CHECK_CALL to false,
                Constant.TOKEN to token
            ).also {
                openActivity(VoiceCallActivity::class.java, it, Constant.USER)
            }
        }
        layoutCallVideo.setOnClickListener {
            getUser()?.run {
                presenter.pushNotifyVoiceCall(
                    token.toString(), userName.toString(),
                    getString(R.string.call_from), id.toString()
                )
            }
            bundleOf(
                Constant.USER to userReceiver, Constant.CHECK_CALL to false,
                Constant.TOKEN to token
            ).also {
                openActivity(TestActivty::class.java, it, Constant.USER)
            }
        }
    }


    private fun pagination(rcl: RecyclerView) {
        rcl.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val lastItem = linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                val firstItem = linearLayoutManager?.findFirstCompletelyVisibleItemPosition()
                // Check if scroll to bottom, it have massage then auto scroll bottom
                if (lastItem == messages.size - 1 && linearLayoutManager != null) {
                    isBottom = true
                } else if (isBottom) {
                    isBottom = false
                }
                /*  if (firstItem == 0 && linearLayoutManager != null && !isLoading) {
                      toast("load new")
                      presenter.loadOldMessage(nodeChild, topNode.toString())
                      isLoading = true
                  }*/
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun loadFirstMessageSuccess(list: ArrayList<MessageModel>) {
        topNode = list[0].id
        messages.addAll(list)
        adapter?.notifyDataSetChanged()
        rclChat.scrollToPosition(messages.size - 1)
        isLoading = false
        loadNewMessage()
        pagination(rclChat)
    }

    override fun loadNodeChildSuccess(node: String) {
        nodeChild = node
    }

    override fun nullNodeChild() {
        if (isNull) {
            if (nodeChild.isEmpty()) {
                nodeChild = getUser()?.id + userReceiver?.id
            }
            loadNewMessage()
            isNull = false
        }
    }

    override fun loadOldMessageSuccess(list: ArrayList<MessageModel>) {
        topNode = list[0].id
        messages.addAll(0, list)
        adapter?.notifyItemRangeInserted(0, messages.size)
        isLoading = false
    }

    override fun loadNewMessageSuccess(model: MessageModel) {
        if (messages.size > 0) {
            if (messages[messages.size - 1].id != model.id) {
                messages.add(model)
                adapter?.notifyItemInserted(messages.size - 1)
            }
        } else {
            messages.add(model)
            adapter?.notifyItemInserted(messages.size - 1)
        }
        if (model.sender == getUser()?.id || isBottom) {
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
        if (userReceiver != null && nodeChild.isNotEmpty()) {
            presenter.sentMessage(nodeChild, messageModel, userReceiver!!)
        }
        gifs.clear()
        adapterGif.notifyDataSetChanged()
    }

    override fun loadGifSuccess(list: ArrayList<GifModel>) {
        gifs.addAll(list)
        rclGif.visible()
        adapterGif.notifyDataSetChanged()
//        isLoadingGif = false
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
        imgRecordAudio.gone()
        imgChooseImg.gone()
    }

    private fun showItemOption() {
        imgCamera.visible()
        imgRecordAudio.visible()
        imgChooseImg.visible()
    }

    private fun loadNewMessage() {
        if (isLoadNew) {
            if (nodeChild.isNotEmpty()) {
                presenter.loadNewMessage(nodeChild!!)
                isLoadNew = false
            }
        }
    }

    override fun sendListPhoto(urls: ArrayList<String>) {
        var messageModel = MessageModel(
            sender = getUser()?.id,
            received = userReceiver?.id,
            isSend = Constant.RECEIVED
        )
        getUser()?.let { presenter.sendAttach(nodeChild, it, urls, messageModel) }
    }

    override fun onClickAudio(uri: Uri, time: String) {
        var messageModel = MessageModel(
            sender = getUser()?.id,
            received = userReceiver?.id,
            isSend = Constant.RECEIVED,
            duration = time
        )
        getUser()?.let {
            presenter.sendAttach(
                nodeChild,
                it,
                messageModel = messageModel,
                uriAudio = uri
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_call -> {
                isShowCall = if (isShowCall) {
                    layoutCall.visible()
                    false
                } else {
                    layoutCall.gone()
                    true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun itemClick(messageModel: MessageModel) {

    }
}