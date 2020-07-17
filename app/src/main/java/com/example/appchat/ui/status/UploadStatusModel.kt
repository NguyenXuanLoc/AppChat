package com.example.appchat.ui.status

import android.net.Uri
import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.common.KeyStatus
import com.example.appchat.data.StatusModel
import com.example.appchat.data.UserModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class UploadStatusModel(statusResponse: UploadStatusResponse) {
    private var v = statusResponse

    fun uploadFile(uri: Uri, file: String? = Constant.IMAGE, attach: Int) {
        var mStorageRef = FirebaseStorage.getInstance().reference;
        val riversRef =
            mStorageRef.child(file.toString()).child(System.currentTimeMillis().toString())
        riversRef.putFile(uri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uri = task.result
                Log.e("TAG", "Attach $attach $uri")
            } else Log.e("TAG", "ERROR")
        }.addOnSuccessListener {
            var result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                Log.e("TAG", it.toString())
            }
        }
    }

    fun uploadStatus(status: String, userModel: UserModel) {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val database = Firebase.database.getReference(Key.STATUS)
        val key = database.push().key
        var model = StatusModel(
            key.toString(),
            userModel.id.toString(),
            status,
            "0",
            currentTime,
            currentDate
        )
        database.child(key.toString()).setValue(model).addOnSuccessListener {
            v.getKeyStatus(key.toString())
        }

    }

    fun uploadUrlToStatus(url: String, Attach: Int) {
        when (Attach) {
            //0: Audio
            //1: Video
            //2: Image
            0 -> {
            }
            1 -> {
            }
            2 -> {
            }
        }
    }
}
