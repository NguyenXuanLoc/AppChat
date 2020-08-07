package com.example.appchat.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.editprofile.EditProfileActivity
import com.example.appchat.ui.home.HomeActivity
import com.example.appchat.ui.register.RegisterActivity
import com.example.fcm.common.ext.*
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_forgot_pass.*


class LoginActivity : BaseActivity(), LoginView {
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: Dialog
    private lateinit var presenter: LoginPresenter
    private val RC_SIGN_IN = 111
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignIn: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager


    override fun contentView(): Int {
        return R.layout.activity_login
    }

    override fun init() {
        hideToolbarBase()
        presenter = LoginPresenter(this, this)
        auth = FirebaseAuth.getInstance()

        dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_forgot_pass)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.id_web_token))
            .requestEmail()
            .build()
        mGoogleSignIn = GoogleSignIn.getClient(this, gso)
        callbackManager = CallbackManager.Factory.create()
    }

    override fun eventHandle() {
        btnLogin.setOnClickListener {
            pbLoading.visible()
            presenter.loginWithEmailAndPass(auth, edtEmail.text.toString(), edtPass.text.toString())
        }

        btnRegister.setOnClickListener { openActivity(RegisterActivity::class.java) }

        lblForgot.setOnClickListener { dialog.show() }

        dialog.btnRequest.setOnClickListener {
            presenter.forgotPass(auth, dialog.tilEmail.editText?.text.toString())
        }
        dialog.setOnCancelListener { dialog.dismiss() }
        imgGoogle.setOnClickListener {
            presenter.signInGoogle(mGoogleSignIn, this)
        }
        presenter.signInFaceBook(callbackManager, btnFacebook, auth)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            presenter.firebaseAuthWithGoogle(account?.idToken!!, auth)
        }
    }


    override fun checkEmail() {
        toast(getString(R.string.check_email))
        dialog.dismiss()
    }

    override fun lackInfo() {
        pbLoading.invisible()
        toast(getString(R.string.lack_of_information))
    }

    override fun error() {
        toast(getString(R.string.error))
    }

    override fun wrongInfo() {
        pbLoading.invisible()
        toast(getString(R.string.wrong_info))
    }

    override fun lackOfInformation() {
        toast(getString(R.string.lack_of_information))
    }

    override fun updateUI(user: FirebaseUser) {
        if (user != null) {
        }
    }

    override fun getUser(user: UserModel) {
        bundleOf(Constant.USER to user).run {
            if (user.dateOfBirth!!.isNotEmpty() && user.gender!!.isNotEmpty() && user.userName!!.isNotEmpty()) {
                saveUser(user)
                openActivity(HomeActivity::class.java, this, Constant.USER)
            } else {
                openActivity(EditProfileActivity::class.java, this, Constant.USER)

            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}