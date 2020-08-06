package com.example.appchat.ui.home

class HomeActivityPresenter(view: HomeActivityView) : HomeActivityResponse {
    val v = view
    val model = HomeActivityModel(this)
    fun updateStatus(idUser: String, status: String) {
        model.upLoadStatus(idUser, status)
    }
}
