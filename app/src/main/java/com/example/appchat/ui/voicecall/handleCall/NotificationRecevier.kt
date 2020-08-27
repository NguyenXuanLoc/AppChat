package com.example.appchat.ui.voicecall.handleCall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.voicecall.VoiceCallActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class NotificationRecevier : BroadcastReceiver() {
    private var id: Int? = null
    private var idUser: String? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            id = getIntExtra(Constant.EXTRA_BUTTON_CLICKED, -1);
            idUser = getStringExtra(Constant.ID_SEND)
        }
        when (id) {
            R.id.lblReceive -> {
                if (context != null) {
                    getUserSend(idUser.toString(), context)
                }
            }
            R.id.lblDeject -> {
            }
        }
        context?.let { NotificationManagerCompat.from(it).cancel(Constant.NOTIFY_VOICE_CALL) }
    }

    private fun getUserSend(
        idUser: String,
        ctx: Context
    ) {
        FirebaseDatabase.getInstance().getReference(Key.USER).child(idUser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var userModel = snapshot.getValue<UserModel>()
                    userModel.let {
                        bundleOf(Constant.USER to it, Constant.CHECK_CALL to true)
                            .also { it1 ->
                                var intent = Intent(ctx, VoiceCallActivity::class.java)
                                intent.putExtra(Constant.USER, it1)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                ctx.startActivity(intent)
                            }
                    }
                }

            })
    }
}