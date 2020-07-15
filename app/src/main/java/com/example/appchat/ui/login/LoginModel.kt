package com.example.appchat.ui.login

import android.app.Activity
import android.util.Log
import com.example.appchat.common.Key
import com.example.appchat.data.UserModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import kotlin.collections.HashMap

class LoginModel(loginResponse: LoginResponse) {
    var v = loginResponse
    fun signInGoogle(mGoogleSignInClient: GoogleSignInClient, activity: Activity) {
        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, 111)
    }

    fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        v.updateUI(user)
                    }
                })
    }

    fun signInFacebook(
        callbackManager: CallbackManager,
        btnFacebook: LoginButton,
        auth: FirebaseAuth
    ) {
        btnFacebook.setReadPermissions("email", "public_profile")
        btnFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken, auth)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
                Log.e("TAG", "facebook:onError", error)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken, auth: FirebaseAuth) {
        Log.e("TAG", "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.e("TAG", "signInWithCredential:success ${user?.displayName}")
                    user?.let { v.updateUI(it) }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("TAG", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }

                // ...
            }
    }

    fun loginWithEmailAndPass(auth: FirebaseAuth, email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            v?.lackOfInformation()
        } else
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { v?.updateUI(it) }
                } else {
                    v?.wrongInfo()
                }
            }
    }

    fun checkAccount(user: FirebaseUser) {
        var reference = FirebaseDatabase.getInstance().getReference(Key.USER).child(user.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toString())
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var userModel = snapshot.getValue<UserModel>()
                    userModel?.let { v.getUser(it) }
                } else {
                    insertAccountToFirebase(user)
                }
            }
        })
    }

    private fun insertAccountToFirebase(user: FirebaseUser) {
        val database = Firebase.database
        val myRef = database.getReference(Key.USER).child(user.uid)
        var map = HashMap<String, String>()
        map[Key.ID] = user.uid
        map[Key.USER_NAME] = user.displayName.toString()
        map[Key.IMAGE_URL] = user.photoUrl.toString()
        map[Key.SEX] = ""
        map[Key.AGE] = ""
        map[Key.STATUS] = ""
        myRef.setValue(map).addOnCompleteListener {
            if (it.isSuccessful) checkAccount(user)
        }
    }
}