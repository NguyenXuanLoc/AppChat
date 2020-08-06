package com.example.appchat.ui.listuser

import com.example.appchat.data.model.UserModel

interface UserFragmentResponse {
    fun resultListUser(list: ArrayList<UserModel>)
    fun resultLoadMoreListUser(list: ArrayList<UserModel>)
    fun resultFirstKey(id: String)
    fun resultNull()
}