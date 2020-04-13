package com.wyt.demo.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wyt.woot_base.traffic.TrafficManager
import com.wyt.woot_base.traffic.IByte
import com.wyt.woot_base.traffic.SpeedUnit
import com.wyt.woot_base.traffic.TrafficInfo

class ByteViewModel : ViewModel() {

    val byteLiveData = MutableLiveData<String>()
    val trafficInfoLiveData = MutableLiveData<TrafficInfo?>()

    fun startByteListen(duration:Long,unit: SpeedUnit){
        TrafficManager.addByteListener(object : IByte {
            override fun onFlowChanged(byte: Double) {
                byteLiveData.postValue("$byte${unit.value}")
            }

        },duration,unit)
    }

    fun stopByteListen(){
        trafficInfoLiveData.postValue(TrafficManager.stopByteListener())
    }

}
