package com.example.appchat.ui.chat

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.toast
import com.example.fcm.common.ext.visible
import kotlinx.android.synthetic.main.activity_messaging.*

class ChatActivity : BaseActivity(), ChatView {
    private val presenter by lazy { ChatPresenter(this) }
    private var user: UserModel? = null

    override fun contentView(): Int {
        return R.layout.activity_messaging
    }

    override fun init() {
        hideToolbarBase()
        applyToolbar(toolbar, R.color.white)
        enableHomeAsUp(toolbar) { finish() }
        changeNavigationIcon()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_message, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.MESSAGE)
        if (bundle != null) user = bundle.getSerializable(Constant.MESSAGE) as UserModel?
    }

    override fun eventHandle() {
        user?.let {
            lblNickName.text = it.userName
            lblStatus.text = it.status
        }
        edtMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) imgGift.gone()
                else imgSend.visible()
            }
        })
        presenter.checkNodeChild(getUser()?.id.toString(), user?.id.toString())
//        imgGift.setOnClickListener { presenter.sentMessage()}
    }

    private fun showIconClear(edt: EditText, img: ImageView) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) img.gone()
                else img.visible()
            }
        })

    }

    override fun loadNodeChildSuccess(node: String) {
        toast("Node child: $node")
        Log.e("TAG", "node chide: $node")
    }

    override fun nullNodeChild() {
        toast("have not Child")
    }

}