package com.wyt.demo.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wyt.demo.R
import com.wyt.demo.main.item.CustomItem
import com.wyt.demo.main.item.HomeItem

class CustomAdapter : BaseQuickAdapter<CustomItem, BaseViewHolder>(R.layout.item_custom) {
    override fun convert(helper: BaseViewHolder, item: CustomItem) {
        helper.setImageResource(R.id.icon, item.icon)
            .setText(R.id.name, item.name)
    }

}