package com.example.appchat.widget

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.common.Constant

abstract class Test(var lm: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    var layoutManager = lm
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
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
        super.onScrolled(recyclerView, dx, dy)
    }

    abstract fun loadMore()
    abstract fun isLoading(): Boolean
}

