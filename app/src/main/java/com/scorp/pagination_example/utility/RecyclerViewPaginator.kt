package com.scorp.pagination_example.utility

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewPaginator(
    private val recyclerView: RecyclerView,
    private val loadMore: (Int) -> Unit,
    private val onLast: () -> Boolean = { true }
) : RecyclerView.OnScrollListener() {

    var threshold = 10
    var currentPage: Int = 0

    var endWithAuto = false

    var isLoading = false

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        Log.e("scroll", "fired!! isloading:$isLoading")
        super.onScrolled(recyclerView, dx, dy)
//        pagination()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        Log.e("scroll_state_changed","fired!! isloading:$isLoading")
        pagination()
    }

    private fun pagination(){
        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            val visibleItemCount = it.childCount
            val totalItemCount = it.itemCount
            val firstVisibleItemPosition = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> return
            }

            if (onLast() || isLoading) {
                return
            }

            if (endWithAuto) {
                if (visibleItemCount + firstVisibleItemPosition == totalItemCount) return
            }

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                isLoading = true
                loadMore(++currentPage)
            }
        }
    }

}