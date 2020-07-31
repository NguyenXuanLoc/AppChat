package com.example.appchat.widget

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.common.Constant

abstract class PaginationScrollNestedListener(lm: LinearLayoutManager? = null) :
    NestedScrollView.OnScrollChangeListener {
    private var layoutManager: LinearLayoutManager? = lm

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (!isLoading() && scrollY == ((v?.getChildAt(0)?.measuredHeight ?: 0) -
                    (v?.measuredHeight ?: 0)) && scrollY > oldScrollY
        ) {
            layoutManager?.also { layoutManager ->
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= Constant.PAGE_SIZE
                ) {
                    loadMore()
                }
            }
        }
    }

    fun setLayoutManager(layoutManager: LinearLayoutManager) {
        this.layoutManager = layoutManager
    }

    abstract fun loadMore()
    abstract fun isLoading(): Boolean
}