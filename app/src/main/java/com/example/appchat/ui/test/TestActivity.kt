package com.example.appchat.ui.test

import android.util.Log
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.data.model.StatusModel
import com.example.appchat.ui.base.BaseActivity
import com.example.fcm.common.ext.getUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class TestActivity : BaseActivity() {
    val status by lazy { ArrayList<StatusModel>() }
    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {

    }

    override fun eventHandle() {
        loadDataBottomToTop()
    }

    private fun onClick(position: Int) {}
    fun loadDataBottomToTop() {
        FirebaseDatabase.getInstance().getReference(Key.STATUS).child(getUser()?.id.toString())
            .orderByKey()
            .limitToLast(5)
            .endAt("-MDP4aCbMm4cV3lwNjqM")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { it ->
                            var model = it.getValue<StatusModel>()
                            model?.let { it1 -> status.add(it1) }
                        }
                        if (status.size > 0) {
                            status.forEach { it -> Log.e("TAG", "${it.status}") }
                        }
                    }
                }
            })
    }
}


