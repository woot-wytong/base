package com.wyt.woot_base.util

import androidx.room.TypeConverter
import java.util.*

object ConversionFactory {

    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLongToDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

}
