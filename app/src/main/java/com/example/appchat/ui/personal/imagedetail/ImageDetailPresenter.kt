package com.example.appchat.ui.personal.imagedetail

class ImageDetailPresenter(imageInfoView: ImageDetailView) : ImageDetailResponse {
    var v = imageInfoView
    var model = ImageDetailModel(this)
}