package com.example.appchat.ui.test

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appchat.R
import com.example.appchat.data.model.ImageModel
import com.example.appchat.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test.*


@Suppress("DEPRECATION")
class TestActivity : BaseActivity() {
    private val images by lazy { ArrayList<ImageModel>() }
    private val adapter by lazy { TestAdapter(this, images) { onClick(it) } }
    override fun contentView(): Int {
        return R.layout.activity_test
    }

    override fun init() {
        rclTest.adapter = adapter
        rclTest.setHasFixedSize(true)
        rclTest.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        images.add(
            ImageModel(
                "",
                "",
                "https://firebasestorage.googleapis.com/v0/b/appchat-c717d.appspot.com/o/image%2F1595925396501?alt=media&token=430d20f2-1cd1-4b60-9ee5-3806e43feb1e"
            )
        )
        images.add(
            ImageModel(
                "",
                "",
                "https://firebasestorage.googleapis.com/v0/b/appchat-c717d.appspot.com/o/image%2F1595928196628?alt=media&token=b2ca89ec-16f9-4733-a750-676231197eff"
            )
        )
        images.add(
            ImageModel(
                "",
                "",
                "https://firebasestorage.googleapis.com/v0/b/appchat-c717d.appspot.com/o/image%2Falien%20(5).png?alt=media&token=8ac72d1f-a256-4712-a2a5-45b92272e4b0"
            )
        )
        images.add(
            ImageModel(
                "",
                "",
                "https://firebasestorage.googleapis.com/v0/b/appchat-c717d.appspot.com/o/image%2F1595928254407?alt=media&token=fab6f6ae-cfcf-4fb8-a7b2-a4cc79cc6b14"
            )
        )

        adapter.notifyDataSetChanged()
    }

    override fun eventHandle() {

    }

    private fun onClick(position: Int) {}
}


