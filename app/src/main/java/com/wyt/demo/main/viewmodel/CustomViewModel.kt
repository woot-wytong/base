package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyt.demo.R
import com.wyt.demo.main.item.CustomItem

class CustomViewModel : ViewModel() {
    val customsLiveData = MutableLiveData<List<CustomItem>>()

    fun initData(){
        val list = ArrayList<CustomItem>()
        list.add(CustomItem(R.mipmap.icon_progress,"进度条(球)"))
        list.add(CustomItem(R.mipmap.icon_dial,"环形刻度表盘"))
        list.add(CustomItem(R.mipmap.icon_color_picker,"颜色选择器"))
        list.add(CustomItem(R.mipmap.icon_bubble,"气泡扩散/收缩"))
        list.add(CustomItem(R.mipmap.icon_search_i,"搜索栏"))
        list.add(CustomItem(R.mipmap.icon_number,"数字输入框"))
        list.add(CustomItem(R.mipmap.icon_select,"框选工具"))
        customsLiveData.postValue(list)
    }
}
