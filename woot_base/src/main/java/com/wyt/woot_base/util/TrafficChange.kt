package com.wyt.woot_base.util

import android.net.TrafficStats
import java.util.*


/**
 * 流量监听
 * 2019.11.28 -> wyt
 */
object TrafficChange {

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
    private var isWorking = false


    /**
     * 注意！！！使用timer，监听结果不可以直接运行再UI线程中
     *
     * @duration 间隔时长(ms)
     * @unit 单位 SpeedUnit.***
     */
    fun addByteListener(listener: IByte, duration: Long, unit: SpeedUnit) {
        if (isWorking) {
            stopByteListener()
        } else {
            isWorking = true
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (preByte != 0L) {
                        val avgByte =
                            when (unit) {
                                SpeedUnit.Mbitps -> {
                                    SpeedUnit.Mbitps.formatValue((totalBytes - preByte).toDouble() / 128 / (duration / 1000)) +
                                            SpeedUnit.Mbitps.value
                                }
                                SpeedUnit.Kbitps -> {
                                    SpeedUnit.Kbitps.formatValue((totalBytes - preByte).toDouble() * 8 / (duration / 1000)) +
                                            SpeedUnit.Kbitps.value

                                }
                                SpeedUnit.MBps -> {
                                    SpeedUnit.MBps.formatValue((totalBytes - preByte).toDouble() / 1024 / (duration / 1000)) +
                                            SpeedUnit.MBps.value

                                }
                                SpeedUnit.KBps -> {
                                    SpeedUnit.KBps.formatValue((totalBytes - preByte).toDouble() / (duration / 1000)) +
                                            SpeedUnit.KBps.value
                                }
                            }
                        listener.getByte(avgByte)
                    }
                    preByte = totalBytes
                }
            }, 0L, duration)
        }
    }

    /**
     * 停止
     */
    fun stopByteListener() {
        preByte = 0L
        isWorking = false
        timer.cancel()
        timer.purge()
    }

}
