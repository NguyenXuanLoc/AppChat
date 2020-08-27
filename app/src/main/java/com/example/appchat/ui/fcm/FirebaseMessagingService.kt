package com.example.appchat.ui.fcm

import android.util.Log
import com.example.appchat.common.Constant
import com.example.appchat.common.util.NotifyUnit
import com.example.appchat.data.model.UserModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject


@Suppress("DEPRECATION")
class FirebaseMessagingService : FirebaseMessagingService() {
    var title: String? = null
    var message: String? = null
    var idSender: String? = null
    var userModel: UserModel? = null
    var options: String? = null

    override fun onMessageReceived(p0: RemoteMessage) {
        var ob = JSONObject(p0.data as Map<*, *>)
        options = p0.data[Constant.OPTIONS]
        title = p0.data[Constant.TITLE]
        message = p0.data[Constant.MESSAGE]
        idSender = p0.data[Constant.ID_SEND]
        var dataModel =
            Data(title.toString(), message.toString(), idSender.toString(), options.toString())
        when (options) {
            Constant.MESSAGE -> {
                Log.e("TAG","MESSAGE")
                NotifyUnit.sendNotifyNewMessage(this, dataModel)
            }
            Constant.VOICE_CALL -> {
                NotifyUnit.sendNotifyVoiceCall(this, dataModel)
            }
        }
    }
}
