package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyt.demo.R
import com.wyt.demo.main.item.HomeItem

class HomeViewModel : ViewModel() {
    val homeItemsLiveData = MutableLiveData<List<HomeItem>>()

    fun initData(){
        val list = ArrayList<HomeItem>()
        list.add(HomeItem(R.mipmap.icon_custom,"自定义View"))
        list.add(HomeItem(R.mipmap.icon_byte,"流量监听"))
        list.add(HomeItem(R.mipmap.rx,"RxJava应用"))

        homeItemsLiveData.postValue(list)
    }
}
