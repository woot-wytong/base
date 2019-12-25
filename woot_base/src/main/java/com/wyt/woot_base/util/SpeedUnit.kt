package com.wyt.woot_base.util

import java.text.DecimalFormat

enum class SpeedUnit(val value: String) {
    Mbitps("Mbps"), Kbitps("Kbps"), MBps("MB/s"), KBps("KB/s");

    fun formatValue(var1: Double): String? {
        return if (this == Mbitps) {
            String.format("%.1f", var1)
        } else if (this == Kbitps) {
            String.format("%.1f", var1)
        } else if (this == MBps) {
            String.format("%.1f", var1)
        } else if (this == KBps) {
            val var3 = DecimalFormat(",###,###")
            var3.format(var1)
        } else {
            null
        }
    }

}