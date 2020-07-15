package com.example.appchat.ui.register

import com.example.appchat.common.Key
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class RegisterModel(registerResponse: RegisterResponse) {
    private var v = registerResponse

    fun registerAccount(auth: FirebaseAuth, email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    var user = auth.currentUser
                    user?.let { insertAccountToFirebase(it) }
                } else v?.error()
            }
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
        myRef.setValue(map)
        v?.success()
        Timber.e("${user.uid} ${user.displayName.toString()} ${user.photoUrl.toString()}")
    }
}