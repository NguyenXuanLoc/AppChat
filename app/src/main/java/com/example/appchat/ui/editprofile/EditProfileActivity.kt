package com.example.appchat.ui.editprofile

import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImage
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.fcm.common.ext.*
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_edit_profile.*


class EditProfileActivity : BaseActivity(), EditProfileView {
    private val presenter by lazy { EditProfilePresenter(this) }
    private var userModel: UserModel? = null
    override fun contentView(): Int {
        return R.layout.activity_edit_profile
    }

    override fun init() {
        applyToolbar()
        blurImage()
    }

    override fun eventHandle() {
        enableHomeAsUp { finish() }
        changeNavigationIcon(R.drawable.ic_snake)
        userModel?.let {
            sdvAvt.setImageSimple(it.imageUrl, this)
            imgBackground.setImage(it.imageUrl.toString(), this)
            edtName.setText(it.userName)
            edtBirth.setText(it.dateOfBirth)
            edtPhoneNumber.setText(it.phoneNumber)
            if (it.gender.toString().isNotEmpty() && it.gender == Key.FEMALE) {
                rbFeMale.isChecked
            }
        }
        //Listener
        showIconClear(edtName, imgClearName)
        showIconClear(edtStory, imgClearStory)
        showIconClear(edtPhoneNumber, imgClearPhone)
        imgClearName.setOnClickListener { edtName.setText("") }
        imgClearStory.setOnClickListener { edtStory.setText("") }
        imgClearPhone.setOnClickListener { edtPhoneNumber.setText("") }
        imgBirth.setOnClickListener { getDateOfBirth() }
        getGender()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update -> {
                if (checkNullBirth()) {
                    userModel?.gender = lblGender.text.toString()
                    userModel?.userName = edtName.text.toString()
                    userModel?.story = edtStory.text.toString()
                    userModel?.phoneNumber = edtPhoneNumber.text.toString()
                    userModel?.userName = edtName.text.toString()
                    userModel?.let { presenter.updateUser(it) }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDateOfBirth() {
        DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                edtBirth.setText("$dayOfMonth/$month/$year")
                userModel?.dateOfBirth = edtBirth.text.toString()
            }, Key.YEAR, Key.MONTH, Key.DATE
        ).show()
    }

    private fun getGender() {
        rgGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbMale -> {
                    lblGender.text = Key.MALE
                }
                R.id.rbFeMale -> {
                    lblGender.text = Key.FEMALE
                }
            }
        }
    }

    private fun checkNullBirth(): Boolean {
        if (edtBirth.text.isEmpty() || edtName.text.isEmpty()) {
            toast(getString(R.string.not_null_birth_notify_or_name))
            return false
        }
        return true
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

    private fun blurImage() {
        val radius = 25f
        val decorView: View = window.decorView
        val windowBackground = decorView.background
        bvBackground.setupWith(decorView.findViewById(android.R.id.content))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)
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

}