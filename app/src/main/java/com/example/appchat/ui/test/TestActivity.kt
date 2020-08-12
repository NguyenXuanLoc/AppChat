package com.example.appchat.ui.test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.Key
import com.example.appchat.data.model.StatusModel
import com.example.appchat.ui.base.BaseActivity
import com.example.appchat.ui.fcm.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
@Suppress("DEPRECATION")
class TestActivity : BaseActivity() {
    var TAG = "TestActivity"
    var CHANNEL_ID = "Your_channel_id"

    val status by lazy { ArrayList<StatusModel>() }
    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {

    }

    private fun updateToken() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token
        val token = refreshToken?.let { Token(it) }
        FirebaseDatabase.getInstance().getReference(Key.TOKENS)
            .child(firebaseUser!!.uid).setValue(token)
    }

    fun sendNotifications(
        userToken: String,
        title: String,
        message: String, idReceive: String
    ) {
        val data = Data(title, message, idReceive)
        val sender = NotificationSender(data, userToken)
        var client = Client()
        var apiService =
            client.getClient(Constant.URL_FCM)?.create(APIService::class.java)
        apiService?.sendNotification(sender)?.enqueue(object : Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.e("TAG", t.message)
            }

            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success != 1) {
                        Timber.e("Error Push")
                    }
                }
            }
        })
    }


    override fun eventHandle() {
        updateToken()
        btnSend.setOnClickListener {
            FirebaseDatabase.getInstance().getReference(Key.TOKENS)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(Key.ID_TOKEN)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var token = snapshot.getValue<String>()
                        sendNotifications(token.toString(), "Lộc ka", "Chào em !!", "")
                    }
                })
        }
        testSound()
    }

    private fun nitification() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            var name = "App Notification"
            var descrptionText = "this is test"
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descrptionText
            }
            var notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Test")
            .setContentText("OK")
            .setSmallIcon(R.drawable.ic_dots_offline)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
    }

    private fun testSound() {
        var mp = MediaPlayer.create(this, R.raw.notify_1)
//        mp.start()
    }

    private fun onClick(position: Int) {}

}



