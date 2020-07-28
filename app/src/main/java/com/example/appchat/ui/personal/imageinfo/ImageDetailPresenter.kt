package com.example.appchat.ui.personal.imageinfo

class ImageDetailPresenter(imageInfoView: ImageDetailView) : ImageDetailResponse {
    var v = imageInfoView
    var model = ImageDetailModel(this)
}