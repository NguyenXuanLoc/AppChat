package com.example.fcm.common.ext

import com.example.appchat.common.Key
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.example.appchat.data.UserModel
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import timber.log.Timber


fun Context.networkIsConnected(): Boolean {
    try {
        val conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return conMgr?.activeNetworkInfo?.isConnected ?: false
    } catch (e: Exception) {
        Timber.e("$e")
    }

    return false
}

fun Context.getFirebaseInstanceToken(onComplete: (String?) -> Unit) {
    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
        if (it.isSuccessful) {
            it.result?.run {
                onComplete(token)
            } ?: onComplete(null)
        } else {
            onComplete(null)
        }
    }
}

fun Context.saveUser(mPrefs: SharedPreferences, userModel: UserModel) {
    var prefsEditor = mPrefs.edit();
    var gson = Gson();
    var json: String = gson.toJson(userModel);
    prefsEditor.putString(Key.USER, json);
    prefsEditor.commit();
}

fun Context.getUser(mPrefs: SharedPreferences): UserModel? {
    val gson = Gson()
    val json: String? = mPrefs.getString(Key.USER, "")
    var user = gson.fromJson(json, UserModel::class.java)
    return user
}

fun Context.removeUser(mPrefs: SharedPreferences) {
    mPrefs.edit().remove(Key.USER).commit()
}

