package com.example.appchat.widget

import androidx.core.widget.NestedScrollView

abstract class PaginationScrollNestedListener : NestedScrollView.OnScrollChangeListener {

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (scrollY == ((v?.getChildAt(0)?.measuredHeight ?: 0) - (v?.measuredHeight
                ?: 0)) && !isLoading() && scrollY > oldScrollY
        ) {
            loadMore()
        }
    }

    abstract fun loadMore()
    abstract fun isLoading(): Boolean
}