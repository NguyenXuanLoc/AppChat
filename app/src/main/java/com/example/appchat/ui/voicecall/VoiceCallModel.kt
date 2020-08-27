package com.example.appchat.ui.voicecall

import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.CallModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class VoiceCallModel(var response: VoiceCallResponse) {
    var v = response

    // check node exits
    fun checkNode(idSender: String, idReceive: String, count: Int = 1) {
        var nodeChild = idSender + idReceive
        if (count <= 2) {
            FirebaseDatabase.getInstance().getReference(Key.CALLING).child(nodeChild)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChildren()) {
                            snapshot.children.forEach { it ->
                                var model = it.getValue<CallModel>()
                                Log.e("TAG", model?.id)
                            }
                        } else {
                            var counts = count + 1
                            checkNode(idReceive, idSender, counts)
                            if (count == 2) {
//                                v.loadNodeChildSuccess(Constant.NULL)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

    // if receive deject call-> upload status to sender listener
    fun getNodeVoiceCall(nodeChild: String) {
        FirebaseDatabase.getInstance().getReference(Key.VOICE_CALL).child(nodeChild).limitToLast(1)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    var model = snapshot.getValue<CallModel>()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    var model = snapshot.getValue<CallModel>()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun upLoadStatusVoiceCall(
        nodeChild: String,
        idSender: String,
        idReceive: String,
        status: String
    ) {
        var ref = FirebaseDatabase.getInstance().getReference(Key.VOICE_CALL).child(nodeChild)
        var id = ref.push().key.toString()
        var model = CallModel(id, idSender, idReceive, status)
        ref.child(id).setValue(model).addOnSuccessListener {
//            v.uploadStatusCallSuccess(model)
        }
    }
}