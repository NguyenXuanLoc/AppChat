package com.example.appchat.ui.personal

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.R
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.StatusModel
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.personal.statusadapter.StatusAdapter
import com.example.appchat.ui.personal.uploadstatus.UploadStatusActivity
import com.example.appchat.widget.PaginationScrollNestedListener
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.openActivity
import kotlinx.android.synthetic.main.frag_personal.*
import kotlinx.android.synthetic.main.frag_personal.view.*



class FragPersonal : BaseFragment(), FragPersonalView {
    private val presenter by lazy { FragPersonalPresenter(this) }

    private val status by lazy { ArrayList<StatusModel>() }
    private val adapter by lazy {
        StatusAdapter(
            this!!.activity!!,
            status
        ) { onClick(it) }

    }
    private var idUser: String? = null

    //Pagination
    private var lastKey: String? = null
    private var lastNode: String? = null
    private var isLoading = false

    private val pagination by lazy {
        object : PaginationScrollNestedListener() {
            override fun loadMore() {
                isLoading = true
                adapter.addLoadingView()
                presenter.loadMore(idUser.toString(), lastNode.toString())
            }
            override fun isLoading(): Boolean {
                return isLoading
            }
        }
    }

    override fun onCreateView(): Int {
        return R.layout.frag_personal
    }

    override fun init() {
        mView.rclStatuss.adapter = adapter
        mView.rclStatuss.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.rclStatuss.setHasFixedSize(true)
        mView.rclStatuss.isNestedScrollingEnabled = false

        self?.let {
            var url = it.getUser()?.imageUrl.toString()
            mView.sdvAvt.setImageSimple(url, it)
            idUser = it.getUser()?.id
        }
    }

    override fun eventHandle() {
        presenter.getStatus(idUser.toString())
        presenter.getLastKey(idUser.toString())
        //Pagination
        mView.nestedScroll.setOnScrollChangeListener(pagination)

        //Listener
        mView.btnUpload.setOnClickListener {
            openActivity(UploadStatusActivity::class.java)
        }
    }

    private fun onClick(statusModel: StatusModel) {

    }

    override fun loadStatusSuccess(results: ArrayList<StatusModel>) {
        if (results.size > 0) {
            status.addAll(results)
            adapter?.notifyDataSetChanged()
            lastNode = results[results.size - 1].id
            imgDecorate.gone()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_personal, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}