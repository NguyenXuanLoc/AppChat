package com.example.appchat.ui.login

import android.app.Activity
import com.example.appchat.data.UserModel
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginPresenter(loginView: LoginView) : LoginResponse {
    var v: LoginView = loginView
    var model = LoginModel(this)
    fun loginWithEmailAndPass(auth: FirebaseAuth, email: String, pass: String) {
        model.loginWithEmailAndPass(auth, email, pass)
    }

    fun signInGoogle(mGoogleSignInClient: GoogleSignInClient, activity: Activity) {
        model.signInGoogle(mGoogleSignInClient, activity)
    }

    fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth) {
        model.firebaseAuthWithGoogle(idToken, auth)
    }

    fun forgotPass(auth: FirebaseAuth, email: String) {
        if (email.isEmpty()) v.lackInfo()
        else
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) v.checkEmail()
                    else v.error()
                }
    }

    fun signInFaceBook(
        callbackManager: CallbackManager,
        btnFacebook: LoginButton,
        auth: FirebaseAuth
    ) {
        model.signInFacebook(callbackManager, btnFacebook, auth)
    }

    override fun updateUI(user: FirebaseUser) {
        model.checkAccount(user)
        v.updateUI(user)
    }

    override fun lackOfInformation() {
        v.lackOfInformation()
    }

    override fun wrongInfo() {
        v.wrongInfo()
    }



    override fun getUser(userModel: UserModel) {
        v.getUser(userModel)
    }
}