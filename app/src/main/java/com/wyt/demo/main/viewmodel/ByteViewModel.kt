package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyt.woot_base.util.ByteChange
import com.wyt.woot_base.util.IByte
import com.wyt.woot_base.util.SpeedUnit

class ByteViewModel : ViewModel() {

    val byteLiveData = MutableLiveData<String>()
    private val byteUtils = ByteChange()

    fun startByteListen(duration:Long,unit: SpeedUnit){
        byteUtils.addByteListener(object : IByte {
            override fun getByte(byte: String) {
                byteLiveData.postValue(byte)
            }
        },duration,unit)
    }

    fun stopByteListen(){
        byteUtils.stopByteListener()
    }

}
