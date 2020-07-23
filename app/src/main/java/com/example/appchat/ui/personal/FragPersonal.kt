package com.example.appchat.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.R
import com.example.appchat.data.model.StatusModel
import com.example.appchat.ui.personal.statusadapter.StatusAdapter
import com.example.appchat.ui.personal.uploadstatus.UploadStatusActivity
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.openActivity
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.frag_personal.*
import kotlinx.android.synthetic.main.frag_personal.view.*

class FragPersonal : Fragment(), FragPersonalView {
    private val presenter by lazy { FragPersonalPresenter(this) }
    private lateinit var mView: View
    private val status by lazy { ArrayList<StatusModel>() }
    private val adapter by lazy { activity?.let {
        StatusAdapter(
            it,
            status
        ) { onClick(it) }
    } }
    private val self by lazy { activity }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Fresco.initialize(self)
        mView = inflater.inflate(R.layout.frag_personal, container, false)
        init()
        eventHandle()
        return mView
    }

    private fun init() {
        mView.rclStatus.layoutManager =
            LinearLayoutManager(self, LinearLayoutManager.VERTICAL, false)
        mView.rclStatus.setHasFixedSize(true)
        mView.rclStatus.adapter = adapter
    }

    private fun eventHandle() {
        presenter.getStatus(self?.getUser()?.id.toString())
        //Listener
        mView.btnUpload.setOnClickListener {
            openActivity(UploadStatusActivity::class.java)
        }

    }

    private fun onClick(statusModel: StatusModel) {

    }

    override fun loadStatusSuccess(statusModel: StatusModel) {
        imgDecorate.gone()
        status.add(statusModel)
        adapter?.notifyDataSetChanged()
    }

}