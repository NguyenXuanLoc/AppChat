package com.example.appchat.ui.personal

import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.frag_personal.*
import kotlinx.android.synthetic.main.frag_personal.view.*


@Suppress("DEPRECATION")
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
    private var firstKey: String? = null
    private var lastKey: String? = null
    private var isLoading = false

    private val pagination by lazy {
        object : PaginationScrollNestedListener() {
            override fun loadMore() {
                isLoading = true
                adapter.addLoadingView()
                presenter.loadMore(idUser.toString(), lastKey.toString())
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
        mView.rclStatus.adapter = adapter
        var manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.rclStatus.setHasFixedSize(true)
        mView.rclStatus.layoutManager = manager

        self?.let {
            var url = it.getUser()?.imageUrl.toString()
            mView.sdvAvt.setImageSimple(url, it)
            idUser = it.getUser()?.id
        }
        setHasOptionsMenu(true)
        changeNavigationIcon(R.drawable.ic_snake)
        pagination.setLayoutManager(mView.rclStatus.layoutManager as LinearLayoutManager)
    }

    override fun eventHandle() {
        dialogLoading?.show()
        presenter.getFirstKey(idUser.toString())
        presenter.loadNewStatus(idUser.toString())
        mView.nestedScroll.setOnScrollChangeListener(pagination)
        //Listener
        mView.btnUpload.setOnClickListener {
            openActivity(UploadStatusActivity::class.java)
        }

    }

    private fun onClick(statusModel: StatusModel) {

    }

    override fun loadMoreSuccess(results: ArrayList<StatusModel>) {
        if (results.size > 0) {
            mView.rclStatus.scrollToPosition(adapter.itemCount - 1);
            imgDecorate.gone()
            adapter.removeLoadingView()
            if (dialogLoading!!.isShowing) dialogLoading?.dismiss()
            for (i in results.size - 1 downTo 0) {
                status.add(results[i])
            }
            lastKey = status[status.size - 1].id
            if (firstKey != lastKey) {
                isLoading = false
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun loadNewStatusSuccess(results: ArrayList<StatusModel>) {
        if (results.size > 0) {
            imgDecorate.gone()
            if (dialogLoading!!.isShowing) dialogLoading?.dismiss()
            for (i in results.size - 1 downTo 0) {
                status.add(results[i])
            }
            lastKey = status[status.size - 1].id
            adapter.notifyDataSetChanged()
        }
    }

    override fun loadFirstKeySuccess(firstKey: String) {
        this.firstKey = firstKey
    }

    override fun nullResult() {
        if (status.size > 0) {
            adapter.removeLoadingView()
        }
        isLoading = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_personal, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_gift -> {
                toast("Bấm cc 2")
            }
            R.id.menu_setting -> {
                toast("Bấm cc 1")
            }
            else -> toast("Bấm cc 3")
        }
        return super.onOptionsItemSelected(item)
    }
}