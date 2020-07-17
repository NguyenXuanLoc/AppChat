package com.example.appchat.ui.playvideo

class PlayVideoPresenter(playVideoView: PlayVideoView) :PlayVideoResponse{
    val v = playVideoView
    val model= PlayVideoModel(this)
}