package com.example.appchat.ui.listuser

import com.example.appchat.data.model.OptionModel
import com.example.appchat.data.model.UserModel

interface UserFragmentView {
    fun resultLoadMoreList(list: ArrayList<UserModel>)
    fun resultListUser(list: ArrayList<UserModel>)
    fun resultFirstKey(firstKey: String)
    fun resultNull()
    fun resultOptions(list: ArrayList<OptionModel>)
}