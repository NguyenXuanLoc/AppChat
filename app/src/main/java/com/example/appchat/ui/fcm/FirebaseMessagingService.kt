package com.example.appchat.ui.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import bundleOf
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.chat.ChatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    var title: String? = null
    var message: String? = null
    var idReceive: String? = null
    var userModel: UserModel? = null
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        title = p0.data[Constant.TITLE]
        message = p0.data[Constant.MESSAGE_RECEIVED]
        idReceive = p0.data[Constant.ID_RECEIVE]
        getUserSend(idReceive.toString())
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel =
                NotificationChannel(Constant.CHANNEL_ID, Constant.LIT_MATCH, importance).apply {
                    description = Constant.DESCRIPTION
                }
            var notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        var intent = Intent(this, ChatActivity::class.java)
        bundleOf(Constant.MESSAGE to userModel).also {
            intent.putExtra(Constant.MESSAGE, it)
        }
        var pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder = NotificationCompat.Builder(this, Constant.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
        var mp = MediaPlayer.create(this, R.raw.notify_1)
        mp.start()
    }

    private fun getUserSend(idUser: String) {
        FirebaseDatabase.getInstance().getReference(Key.USER).child(idUser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    userModel = snapshot.getValue<UserModel>()
                    Log.e("TAG", userModel?.userName)
                }

            })
    }
}