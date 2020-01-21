package com.wyt.woot_base.util

import java.text.DecimalFormat

enum class SpeedUnit(val value: String) {
    Mbitps("Mbps"), Kbitps("Kbps"), MBps("MB/s"), KBps("KB/s");

    fun formatValue(var1: Double): String? {
        return when {
            this == Mbitps -> {
                String.format("%.1f", var1)
            }
            this == Kbitps -> {
                String.format("%.1f", var1)
            }
            this == MBps -> {
                String.format("%.1f", var1)
            }
            this == KBps -> {
                val var3 = DecimalFormat(",###,###")
                var3.format(var1)
            }
            else -> {
                null
            }
        }
    }

}