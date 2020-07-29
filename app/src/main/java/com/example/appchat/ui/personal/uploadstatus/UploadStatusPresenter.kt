package com.example.appchat.ui.personal.uploadstatus

import android.net.Uri
import com.example.appchat.common.Constant
import com.example.appchat.data.model.UserModel

class UploadStatusPresenter(statusView: UploadStatusView) : UploadStatusResponse {
    private val model = UploadStatusModel(this)
    private val v = statusView

    //0: Audio    //1: Video    //2: Image
    fun uploadFile(
        uriList: ArrayList<Uri>? = null,
        uriAudio: Uri? = null,
        uriVideo: Uri? = null,
        attach: Int,
        idStatus: String, idUser: String,
        uriThumbnail: Uri? = null, duration: String? = null
    ) {
        uriAudio?.let {
            model.uploadFile(uriAudio, Constant.AUDIO, attach, idStatus, idUser)
            duration?.let {
                model.uploadDuration(duration, idStatus, idUser)
            }
        }
        uriVideo?.let {
            model.uploadFile(uriVideo, Constant.VIDEO, attach, idStatus, idUser)
            uriThumbnail?.let { model.uploadThumbnail(uriThumbnail, 3, idStatus, idUser) }
        }
        uriList?.let {
            for (i in 0 until uriList.size) {
                model.uploadFile(uriList[i], Constant.IMAGE, attach, idStatus, idUser)
            }
        }
    }

    fun uploadStatus(status: String, user: UserModel) {
        model.uploadStatus(status, user)
    }

    override fun getKeyStatus(idStatus: String) {
        v.getIdStatus(idStatus)
    }

    override fun uploadSuccess() {
        v.uploadSuccess()
    }


}