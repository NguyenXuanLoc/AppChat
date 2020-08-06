package com.example.appchat.ui.test

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseFragment
import com.example.fcm.common.ext.toast
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class FragTest : BaseFragment() {

    override fun eventHandle() {
    }

    override fun init() {
        toast("OK")
    }

    override fun onCreateView(): Int {
        return R.layout.activity_register
    }

    private fun randomAvt(ctx: Context): String {
        var list = ctx.resources.getStringArray(R.array.image)
        var index = Random().nextInt(list.size)
        Log.e("TAG", list[index])
        return list[index]
    }

    fun addUser() {
        var phones = arrayOf("0966468393", "0966666854", "09664754856", "09625456543")
        var gender = arrayOf("nam", "nu")
        var names = arrayOf(
            "Nguyễn Linh",
            "Loc.Ka",
            "Châm. Sứa :)",
            "Link.Kak",
            "Tuan.A",
            "Nguyễn Du",
            "Bình Good",
            "Link Anh",
            "ChamCham"
        )
        var status = arrayOf(
            "Bad boy",
            "Fuck Girl",
            "Vui vẻ",
            "Hiền lành",
            "Dễ tính",
            "Lắm mòm",
            "Mõm"
        )
        var date = arrayOf("31/12/1998", "31/12/1997", "31/12/1996", "31/12/1995", "31/12/1995")
        for (i in 0 until 20) {
            toast(i.toString())
            var ref = FirebaseDatabase.getInstance().getReference(Key.USER).push()
            var key = ref.key
            var model = UserModel(
                key,
                names[Random().nextInt(names.size - 1)],
                date[Random().nextInt(date.size - 1)],
                phones[Random().nextInt(phones.size - 1)],
                self?.let { randomAvt(it) },
                gender[Random().nextInt(gender.size - 1)],
                status[Random().nextInt(status.size - 1)],
                "online"
            )
            ref.setValue(model)
        }
    }

}