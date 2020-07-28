package com.example.appchat.ui.personal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.StatusModel
import com.example.appchat.ui.personal.statusadapter.StatusAdapter
import com.example.appchat.ui.personal.uploadstatus.UploadStatusActivity
import com.example.appchat.widget.PaginationScrollListener
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.toast
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.frag_personal.*
import kotlinx.android.synthetic.main.frag_personal.view.*
import timber.log.Timber


class FragPersonal : Fragment(), FragPersonalView {
    private val presenter by lazy { FragPersonalPresenter(this) }
    private lateinit var mView: View
    private val status by lazy { ArrayList<StatusModel>() }
    private val adapter by lazy {
        StatusAdapter(
            this!!.activity!!,
            status
        ) { onClick(it) }

    }
    private val self by lazy { activity }
    private var idUser: String? = null

    //Pagination
    private var lastKey: String? = null
    private var lastNode: String? = null
    private var isLoading = false
    private var hasMoreData = true

    private val paginationScrollListener by lazy {
        object : PaginationScrollListener() {
            override fun loadMoreItems() {
//                isLoading = true
                mView.rclStatuss.post {
                    adapter?.addLoadingView()
                }
                activity?.let { toast("bottom", it) }
            }

            override fun isLastPage(): Boolean {
                return !hasMoreData
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        }
    }


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
        mView.rclStatuss.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.rclStatuss.setHasFixedSize(true)
        mView.rclStatuss.adapter = adapter

        var url = self?.getUser()?.imageUrl.toString()
        activity?.let { mView.sdvAvt.setImageSimple(url, it) }
        idUser = self?.getUser()?.id

//        setPaginationLayoutManager()
//        mView.rclStatus.addOnScrollListener(paginationScrollListener)
    }

    private fun setPaginationLayoutManager() {
        paginationScrollListener.setLayoutManager(mView.rclStatuss.layoutManager as LinearLayoutManager)
    }

    private fun eventHandle() {
        presenter.getStatus(idUser.toString())
        presenter.getLastKey(idUser.toString())
        //Listener
        mView.btnUpload.setOnClickListener {
            openActivity(UploadStatusActivity::class.java)
        }
        paginationScroll(mView.rclStatuss)

    }

    private fun onClick(statusModel: StatusModel) {

    }

    private fun paginationScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading && linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == status.size - 1) {
                    adapter.addLoadingView()
                    presenter.loadMore(idUser.toString(), lastNode.toString())
                    activity?.let { toast("Next", it) }
                    isLoading = true
                }
            }
        })

    }

    override fun loadStatusSuccess(results: ArrayList<StatusModel>) {
        if (results.size > 0) {
            isLoading = false
            status.addAll(results)
            adapter?.notifyDataSetChanged()
            imgDecorate.gone()
            lastNode = results[results.size - 1].id
        }
    }

    override fun loadMoreSuccess(results: ArrayList<StatusModel>) {
        if (results.size > 0) {
            isLoading = false
            adapter?.removeLoadingView()
            status.addAll(results)
            adapter?.notifyDataSetChanged()
            imgDecorate.gone()
            lastNode = results[results.size - 1].id
        }
    }

    override fun nullResult() {
        adapter.removeLoadingView()
        isLoading = true
    }

    override fun resultLastKey(lastKey: String) {
        this.lastKey = lastKey
    }

}