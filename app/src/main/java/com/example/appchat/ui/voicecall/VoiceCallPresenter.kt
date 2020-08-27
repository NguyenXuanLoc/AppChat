package com.example.appchat.ui.voicecall

import com.example.appchat.data.model.CallModel

class VoiceCallPresenter(var view: VoiceCallView) : VoiceCallResponse {
    var model = VoiceCallModel(this)
    var v = view

    fun checkNode(idSender: String, idReceive: String) {
        model.checkNode(idSender, idReceive)
    }

    fun getNodeVoiceCall(nodeChild: String) {
        model.getNodeVoiceCall(nodeChild)
    }

    fun uploadStatusVoiceCall(
        idSender: String,
        idReceive: String,
        status: String,
        nodeChild: String
    ) {
        model.upLoadStatusVoiceCall(nodeChild, idSender, idReceive, status)
    }


}