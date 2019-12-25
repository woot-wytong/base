package com.wyt.demo.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wyt.demo.R
import com.wyt.demo.main.item.HomeItem

class HomeAdapter(
    private val rvWidth: Int,
    private val leftRight: Int,
    private val spanCount: Int
) : BaseQuickAdapter<HomeItem, BaseViewHolder>(R.layout.item_home) {
    override fun convert(helper: BaseViewHolder, item: HomeItem) {
        helper.setImageResource(R.id.icon, item.icon)
            .setText(R.id.name, item.name)
        val itemWidth = (rvWidth - leftRight * (spanCount + 1)) / spanCount
        val params = helper.itemView.layoutParams
        params.width = itemWidth
        helper.itemView.layoutParams = params
    }

}