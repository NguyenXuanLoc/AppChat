package com.example.appchat.ui.status

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber

class StatusModel(statusResponse: StatusResponse) {
    private var v = statusResponse
    fun uploadImage(uri: Uri) {
        var mStorageRef: StorageReference = FirebaseStorage.getInstance().reference;
        val file: Uri = uri
        val riversRef: StorageReference =
            mStorageRef.child(System.currentTimeMillis().toString())
            riversRef.putFile(file)
            .addOnSuccessListener {   // Get a URL to the uploaded content
                Log.e("TAG", "OK")
            }
            .addOnFailureListener {
                Log.e("TAG", "Error")
                // Handle unsuccessful uploads
                // ...
            }
    }
}
