package com.example.appchat.ui.me.imagedetail

class ImageDetailPresenter(imageInfoView: ImageDetailView) : ImageDetailResponse {
    var v = imageInfoView
    var model = ImageDetailModel(this)
}