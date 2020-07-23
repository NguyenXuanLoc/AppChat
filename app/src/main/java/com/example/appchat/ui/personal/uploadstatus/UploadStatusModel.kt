package com.example.appchat.ui.personal.uploadstatus

import android.net.Uri
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class UploadStatusModel(statusResponse: UploadStatusResponse) {
    private var v = statusResponse

    fun uploadFile(
        uri: Uri,
        namePath: String,
        attach: Int,
        idStatus: String
    ) {
        var mStorageRef = FirebaseStorage.getInstance().reference;
        val riversRef =
            mStorageRef.child(namePath.toString()).child(System.currentTimeMillis().toString())
        riversRef.putFile(uri).addOnSuccessListener { it ->
            var result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                uploadAttachStatus(it.toString(), idStatus, attach)
            }
        }
    }

    fun uploadStatus(status: String, userModel: UserModel) {
        val currentDate: String = SimpleDateFormat("dd-MM", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
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
        database.child(userModel.id.toString()).child(key.toString()).setValue(model)
            .addOnSuccessListener {
                v.getKeyStatus(key.toString())
            }
    }

    private fun uploadAttachStatus(
        url: String,
        idStatus: String,
        attach: Int
    ) {
        var nameFile: String? = null
        var database = Firebase.database
        when (attach) {
            //0: Audio          //1: Video          //2: Image
            0 -> {
                var ref = database.getReference(Constant.AUDIO)
                var key = ref.push().key.toString()
                var audioModel =
                    AudioModel(key, idStatus, url)
                ref.child(idStatus).push().setValue(audioModel)
                    .addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
            1 -> {
                var ref = database.getReference(Constant.VIDEO)
                var key = ref.push().key.toString()
                var videoModel =
                    VideoModel(key, idStatus, url)
                ref.child(idStatus).push().setValue(videoModel)
                    .addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
            2 -> {
                var ref = database.getReference(Constant.IMAGE)
                var key = ref.push().key.toString()
                var imageModel =
                    ImageModel(key, idStatus, url)
                ref.child(idStatus).push().setValue(imageModel)
                    .addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
        }
    }
}
