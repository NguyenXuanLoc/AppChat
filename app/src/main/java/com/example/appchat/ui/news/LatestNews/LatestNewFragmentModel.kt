package com.example.appchat.ui.news.LatestNews

import com.example.appchat.common.Key
import com.google.firebase.database.FirebaseDatabase

class LatestNewFragmentModel(response: LatestNewFragmentResponse) {
    var v = response
    fun getLastNews(){
        FirebaseDatabase.getInstance().getReference(Key.STATUS)
    }
}