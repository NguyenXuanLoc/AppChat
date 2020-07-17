package com.example.appchat.ui.status

import android.net.Uri
import com.example.appchat.common.Constant
import com.example.appchat.data.UserModel

class UploadStatusPresenter(statusView: UploadStatusView) : UploadStatusResponse {
    private val model = UploadStatusModel(this)
    private val v = statusView

    //0: Audio    //1: Video    //2: Image
    fun uploadFile(
        uriList: ArrayList<Uri>? = null, uriAudio: Uri? = null,
        uriVideo: Uri? = null, name: String = Constant.IMAGE, attach: Int
    ) {
        uriAudio?.let { model.uploadFile(uriAudio, name, attach) }
        uriVideo?.let { model.uploadFile(uriVideo, name, attach) }
        uriList?.let {
            for (i in 0 until uriList.size)
                model.uploadFile(uriList[i], name, attach)
        }
    }

    fun uploadStatus(status: String, user: UserModel) {
        model.uploadStatus(status, user)
    }

    override fun getKeyStatus(idStatus: String) {
        v.getIdStatus(idStatus)
    }


}