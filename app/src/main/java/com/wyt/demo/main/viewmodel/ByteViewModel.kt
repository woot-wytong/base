package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyt.woot_base.util.TrafficChange
import com.wyt.woot_base.util.IByte
import com.wyt.woot_base.util.SpeedUnit

class ByteViewModel : ViewModel() {

    val byteLiveData = MutableLiveData<String>()

    fun startByteListen(duration:Long,unit: SpeedUnit){
        TrafficChange.addByteListener(object : IByte {
            override fun getByte(byte: String) {
                byteLiveData.postValue(byte)
            }
        },duration,unit)
    }

    fun stopByteListen(){
        TrafficChange.stopByteListener()
    }

}
