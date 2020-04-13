package com.wyt.woot_base.traffic

enum class SpeedUnit(val value: String) {
    Mbitps("Mbps"), Kbitps("Kbps"), MBps("MB/s"), KBps("KB/s");

    fun formatValue(var1: Double): String {
        return when {
            this == Mbitps -> {
                String.format("%.2f", var1)
            }
            this == Kbitps -> {
                String.format("%.2f", var1)
            }
            this == MBps -> {
                String.format("%.2f", var1)
            }
            this == KBps -> {
                String.format("%.2f", var1)
            }
            else -> {
                "0.00"
            }
        }
    }

}