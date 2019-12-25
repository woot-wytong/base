package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class RxJavaViewModel : ViewModel() {
    val msgLiveData = MutableLiveData<String>()

    fun initData(){
        val s =
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
}
