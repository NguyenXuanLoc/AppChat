package com.example.appchat.ui.status

import android.net.Uri
import com.example.appchat.common.Constant
import com.example.appchat.data.UserModel

class UploadStatusPresenter(statusView: UploadStatusView) : UploadStatusResponse {
    private val model = UploadStatusModel(this)
    private val v = statusView

    //0: Audio    //1: Video    //2: Image
    fun uploadFile(
        uriList: ArrayList<Uri>? = null,
        size: Int? = null,
        uriAudio: Uri? = null,
        uriVideo: Uri? = null,
        attach: Int,
        idStatus: String
    ) {
        var name: String? = null
        when (attach) {
            0 -> name = Constant.AUDIO
            1 -> name = Constant.VIDEO
            2 -> name = Constant.IMAGE
        }
        uriAudio?.let { model.uploadFile(uriAudio, name, attach, idStatus) }
        uriVideo?.let { model.uploadFile(uriVideo, name, attach, idStatus) }
        uriList?.let {
            for (i in 0 until uriList.size) {
                model.uploadFile(uriList[i], name, attach, idStatus, size, i)
            }
        }
    }

    fun uploadStatus(status: String, user: UserModel, attach: Int) {
        model.uploadStatus(status, attach, user)
    }

    override fun getKeyStatus(idStatus: String) {
        v.getIdStatus(idStatus)
    }

    override fun uploadSuccess() {
        v.uploadSuccess()
    }


}