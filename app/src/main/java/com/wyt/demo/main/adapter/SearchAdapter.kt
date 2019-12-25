package com.wyt.demo.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wyt.demo.R
import com.wyt.demo.main.item.SearchItem

class SearchAdapter : BaseQuickAdapter<SearchItem, BaseViewHolder>(R.layout.item_search) {
    override fun convert(helper: BaseViewHolder, item: SearchItem) {
        helper.setText(R.id.search_name,item.name)
    }
}