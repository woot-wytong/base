package com.wyt.woot_base.traffic

import android.net.TrafficStats
import java.util.*


/**
 * 流量监听
 * 2019.11.28 -> wyt
 */
object TrafficManager {

    private lateinit var trafficInfo: TrafficInfo

    private//总的接受字节数，包含Mobile和WiFi等
    val totalDownBytes: Long
        get() = if (TrafficStats.getUidRxBytes(android.os.Process.myUid()) == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getUidRxBytes(
            android.os.Process.myUid()
        ) / 1024

    private//总的发送字节数，包含Mobile和WiFi等
    val totalUpBytes: Long
        get() = if (TrafficStats.getUidTxBytes(android.os.Process.myUid()) == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getUidTxBytes(
            android.os.Process.myUid()
        ) / 1024

    /**
     * 获取手机接收和发送的总字节数
     * @return TotalBytes
     */
    val totalBytes: Long
        get() = totalDownBytes + totalUpBytes

    private lateinit var timer: Timer
    private var preByte: Long = 0L
    private var isRun = false


    /**
     * 注意！！！使用timer，监听结果不可以直接运行在UI线程中
     *
     * @listener 实时平均速率回调
     * @duration 间隔时长(ms)
     * @unit 单位 SpeedUnit.***
     */
    fun addByteListener(listener: IByte, duration: Long, unit: SpeedUnit) {
        if (isRun) {
            return
        }
        isRun = true
        trafficInfo = TrafficInfo()
        trafficInfo.startFlow = totalBytes
        trafficInfo.startTime = System.currentTimeMillis()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (preByte != 0L) {
                    val avgFlowKB = (totalBytes - preByte).toDouble() / (duration / 1000)
                    if (avgFlowKB > trafficInfo.maxFlow) trafficInfo.maxFlow = avgFlowKB
                    val avgByte =
                        when (unit) {
                            SpeedUnit.Mbitps -> {
                                SpeedUnit.Mbitps.formatValue(avgFlowKB / 128)
                                    .toDouble()
                            }
                            SpeedUnit.Kbitps -> {
                                SpeedUnit.Kbitps.formatValue(avgFlowKB * 8)
                                    .toDouble()
                            }
                            SpeedUnit.MBps -> {
                                SpeedUnit.MBps.formatValue(avgFlowKB / 1024)
                                    .toDouble()
                            }
                            SpeedUnit.KBps -> {
                                SpeedUnit.KBps.formatValue(avgFlowKB)
                                    .toDouble()
                            }
                        }
                    listener.onFlowChanged(avgByte)
                } else {
                    trafficInfo.maxFlow = 0.0
                }
                preByte = totalBytes
            }
        }, 0L, duration)
    }

    /**
     * 停止
     */
    fun stopByteListener(): TrafficInfo? {
        if(!isRun){
            return null
        }

        trafficInfo.stopFlow = totalBytes
        trafficInfo.stopTime = System.currentTimeMillis()
        trafficInfo.allFlow = trafficInfo.stopFlow - trafficInfo.startFlow
        trafficInfo.allTime = trafficInfo.stopTime - trafficInfo.startTime
        trafficInfo.avgFlow = (trafficInfo.allFlow.toDouble() / (trafficInfo.allTime / 1000))

        preByte = 0L
        isRun = false
        timer.cancel()
        timer.purge()
        return trafficInfo
    }

}
