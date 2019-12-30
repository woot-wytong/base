package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyt.demo.room.AppDataBase
import com.wyt.demo.room.bean.User
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class RxJavaViewModel : ViewModel() {
    val msgLiveData = MutableLiveData<String>()

    fun initData(){
        Observable
            .interval(3,TimeUnit.SECONDS)
            .subscribe(object :Observer<Long>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Long) {
                msgLiveData.postValue("$t")
            }

            override fun onError(e: Throwable) {

            }
        })

    }

    fun initUser(){
        val list = ArrayList<User>()
        list.add(User(1,"卡萨丁","撒旦","男",15))
        list.add(User(2,"拉克丝的","离开了","男",15))
        list.add(User(3,"空间规划","哦i有","男",15))
        list.add(User(4,"u期望他也","看漫画你","男",15))
        list.add(User(5,"吧v从","i计划以","男",15))
        list.add(User(6,"就宣布支持","NHK了","男",15))
        AppDataBase.default.dao().insert(list)
    }
}
