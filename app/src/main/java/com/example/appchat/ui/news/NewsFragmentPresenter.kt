package com.example.appchat.ui.news

class NewsFragmentPresenter(view: NewsFragmentView) : NewsFragmentResponse {
    var model = NewsFragmentModel(this)
    var v = view
}