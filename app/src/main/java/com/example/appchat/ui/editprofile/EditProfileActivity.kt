package com.example.appchat.ui.editprofile

import android.view.Menu
import android.widget.TextView
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.widget.DialogChooseDate
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.saveUser
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.activity_edit_profile.*


@Suppress("DEPRECATION")
class EditProfileActivity : BaseActivity(), EditProfileView, DialogChooseDate.DateChooseListener {
    private val presenter by lazy { EditProfilePresenter(this) }
    private var userModel: UserModel? = null
    private val dialogDate by lazy { DialogChooseDate(self) }
    private var getGender = ""
    private var getStory = ""

    override fun contentView(): Int {
        return R.layout.activity_edit_profile
    }

    override fun init() {
        hideToolbarBase()
        hideLogo()
        dialogDate.setDateChooseListener(this)
    }

    override fun eventHandle() {
        enableHomeAsUp { finish() }
        changeNavigationIcon(R.drawable.ic_snake)
        userModel?.let { edtName.setText(it.userName) }

        //Listener
        edtDateOfBirth.setOnClickListener {
            dialogDate.show()
        }
        getGender()
        btnFinish.setOnClickListener {
            if (edtName.text.isNotEmpty() && edtDateOfBirth.text.isNotEmpty() && getGender.isNotEmpty()) {
                userModel?.apply {
                    userName = edtName.text.toString()
                    dateOfBirth = edtDateOfBirth.text.toString()
                    gender = getGender
                    story = getStory
                }
                userModel?.let { it1 -> presenter.updateUser(it1) }
            } else {
                toast(getString(R.string.not_null_information))
            }
        }
    }

    private fun getGender() {
        rgGender.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbMale -> {
                    getGender = Key.MALE
                    getStory = getString(R.string.he_is_newbie)
                }
                R.id.rbFemale -> {
                    getGender = Key.FEMALE
                    getStory = getString(R.string.she_is_newbie)
                }
            }
        }
    }

    override fun getExtra() {
        var bundle = intent.getBundleExtra(Constant.USER)
        bundle?.run {
            userModel = getSerializable(Constant.USER) as UserModel
//            userModel?.gender = Key.MALE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun updateSuccess(model: UserModel) {
        saveUser(model)
        openActivity(HomeActivity::class.java)
        toast(getString(R.string.welcome))
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun resultDate(birthDay: String) {
        edtDateOfBirth.setText(birthDay)
    }


}