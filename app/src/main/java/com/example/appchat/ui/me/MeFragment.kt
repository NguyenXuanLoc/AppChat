package com.example.appchat.ui.me

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.R
import com.example.appchat.common.Key
import com.example.appchat.common.ext.setImage
import com.example.appchat.common.ext.setImageSimple
import com.example.appchat.data.model.StatusModel
import com.example.appchat.data.model.UserModel
import com.example.appchat.ui.base.BaseFragment
import com.example.appchat.ui.me.statusadapter.StatusAdapter
import com.example.appchat.ui.uploadstatus.UploadStatusActivity
import com.example.appchat.widget.PaginationScrollNestedListener
import com.example.fcm.common.ext.getUser
import com.example.fcm.common.ext.gone
import com.example.fcm.common.ext.openActivity
import com.example.fcm.common.ext.toast
import kotlinx.android.synthetic.main.fragment_me.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MeFragment : BaseFragment(), MeFragmentView {
    companion object {
        fun newInstance(): MeFragment {
            return MeFragment()
        }
    }

    private var user: UserModel? = null
    private var isViewed = false
    private val presenter by lazy { MeFragmentPresenter(this) }

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
        return R.layout.fragment_me
    }

    override fun init() {
        applyToolbar(mView.toolbar, removeElevation = true)

        mView.rclStatus.adapter = adapter
        var manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.rclStatus.setHasFixedSize(true)
        mView.rclStatus.layoutManager = manager
        self?.let { activity ->
            user = activity.getUser()
            user?.let { user ->
                var url = user.imageUrl
                mView.sdvAvt.setImageSimple(url, activity)
                mView.lblNickName.text = user.userName
                mView.lblAge.text = getAge(user.dateOfBirth.toString())
                mView.lblStory.text = user.story
                idUser = user.id
                if (user.gender == Key.MALE) {
                    mView.imgGender.setImage(R.drawable.ic_male_white, activity)
                } else {
                    mView.imgGender.setImage(R.drawable.ic_female_white, activity)
                }
            }
        }
        pagination.setLayoutManager(mView.rclStatus.layoutManager as LinearLayoutManager)
    }


    override fun eventHandle() {
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
            mView.rclStatus.scrollToPosition(adapter.itemCount - 1)
            adapter.removeLoadingView()
            for (i in results.size - 1 downTo 0) {
                status.add(results[i])
            }
            lastKey = status[status.size - 1].id
            if (firstKey != lastKey) {
                isLoading = false
            }
            adapter.notifyItemInserted(status.size - 1)
        }
    }

    override fun loadNewStatusSuccess(results: ArrayList<StatusModel>) {
        if (results.size > 0) {
            dialogLoading?.dismiss()
            mView.imgDecorate.gone()
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
        dialogLoading?.dismiss()
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

    override fun onResume() {
        super.onResume()
        if (!isViewed) {
            dialogLoading?.show()
            presenter.getFirstKey(idUser.toString())
            presenter.loadNewStatus(idUser.toString())
            isViewed = true
        }
        changeNavigationIcon(R.drawable.ic_snake)
    }

    private fun getAge(dateOfBirth: String): String {
        var year = dateOfBirth.split("/")
        val currentYear: String = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
        var result = currentYear.toInt() - year[2].toInt()
        return result.toString()
    }
}