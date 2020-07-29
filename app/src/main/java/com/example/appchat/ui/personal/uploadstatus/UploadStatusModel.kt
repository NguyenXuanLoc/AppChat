package com.example.appchat.ui.personal.uploadstatus

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.ImageModel
import com.example.appchat.data.model.StatusModel
import com.example.appchat.data.model.UserModel
import com.google.firebase.database.FirebaseDatabase
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
        idStatus: String, idUser: String
    ) {
        var mStorageRef = FirebaseStorage.getInstance().reference
        var nameFile = System.currentTimeMillis().toString()
        val riversRef = mStorageRef.child(namePath).child(nameFile)
        riversRef.putFile(uri).addOnSuccessListener { it ->
            var result = it.metadata?.reference?.downloadUrl
            var name = it.metadata?.name
            result?.addOnSuccessListener {
                uploadAttachStatus(it.toString(), idStatus, attach, idUser, name.toString())
            }
        }
    }

    fun uploadThumbnail(
        uri: Uri,
        attach: Int,
        idStatus: String, idUser: String
    ) {
        var mStorage = FirebaseStorage.getInstance().getReference(Key.IMAGE)
        var nameFile = System.currentTimeMillis().toString()
        mStorage.child(nameFile).putFile(uri).addOnSuccessListener { it ->
            var result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                uploadAttachStatus(it.toString(), idStatus, attach, idUser, nameFile)
            }
        }
    }

    fun uploadDuration(duration: String? = null, idStatus: String, idUser: String) {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(idUser).child(idStatus)
            .child(Key.DURATION).setValue(duration)
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
        attach: Int,
        idUser: String,
        nameFile: String
    ) {
        var database = Firebase.database
        when (attach) {
            //0: Audio          //1: Video          //2: Image  //3.Thumbnail Audio //4. Upload Duration
            0 -> {
                database.getReference(Key.STATUS).child(idUser).child(idStatus).child(Key.AUDIO)
                    .setValue(url).addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
            1 -> {
                database.getReference(Key.STATUS).child(idUser).child(idStatus).child(Key.VIDEO)
                    .setValue(url).addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
            2 -> {
                var imageModel = ImageModel(nameFile, idStatus, url)
                database.getReference(Constant.IMAGE).child(idStatus).push().setValue(imageModel)
                    .addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
            3 -> {
                database.getReference(Key.STATUS).child(idUser)
                    .child(idStatus).child(Key.THUMBNAIL).setValue(url).addOnSuccessListener {
                        v.uploadSuccess()
                    }
            }
            4 -> {

            }
        }
    }


}
