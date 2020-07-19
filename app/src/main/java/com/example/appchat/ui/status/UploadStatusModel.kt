package com.example.appchat.ui.status

import android.net.Uri
import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class UploadStatusModel(statusResponse: UploadStatusResponse) {
    private var v = statusResponse

    fun uploadFile(uri: Uri, file: String? = Constant.IMAGE, attach: Int, idStatus: String) {
        var mStorageRef = FirebaseStorage.getInstance().reference;
        val riversRef =
            mStorageRef.child(file.toString()).child(System.currentTimeMillis().toString())
        riversRef.putFile(uri).addOnSuccessListener { it ->
            var result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                uploadUrlToStatus(it.toString(), idStatus, attach)

            }
        }
    }

    fun uploadStatus(status: String, attach: Int, userModel: UserModel) {
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
            currentDate, attach.toString()
        )
        database.child(key.toString()).setValue(model).addOnSuccessListener {
            v.getKeyStatus(key.toString())
        }

    }

    private fun uploadUrlToStatus(url: String, idStatus: String, attach: Int) {
        var nameFile: String? = null

        when (attach) {
            //0: Audio
            //1: Video
            //2: Image
            0 -> {
                nameFile = Constant.AUDIO
                var database = Firebase.database.getReference(nameFile.toString())
                var key = database.push().key.toString()
                var audioModel = AudioModel(key, idStatus, url)
                database.child(key).setValue(audioModel)
                    .addOnSuccessListener {
                        Log.d("TAG", "update Audio")
                    }
            }
            1 -> {
                nameFile = Constant.VIDEO
                var database = Firebase.database.getReference(nameFile.toString())
                var key = database.push().key.toString()
                var videoModel = VideoModel(key, idStatus, url)
                database.push().setValue(videoModel)
                    .addOnSuccessListener {
                        Log.d("TAG", "update Video")
                    }
            }
            2 -> {
                nameFile = Constant.IMAGE
                var database = Firebase.database.getReference(nameFile.toString())
                var key = database.push().key.toString()
                var imageModel = ImageModel(key, idStatus, url)
                database.child(key).setValue(imageModel)
                    .addOnSuccessListener {
                        Log.d("TAG", "update Image")
                    }
            }
        }
    }
}
