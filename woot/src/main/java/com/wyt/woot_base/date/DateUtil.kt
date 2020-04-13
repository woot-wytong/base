package com.wyt.woot_base.date

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Date转换相关工具类
 */
object DateUtil {

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    @SuppressLint("SimpleDateFormat")
    fun dateToString(data: Date?, formatType: String): String {
        return SimpleDateFormat(formatType).format(data!!)
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    @Throws(ParseException::class)
    fun longToString(currentTime: Long, formatType: String): String {
        val date = longToDate(
            currentTime,
            formatType
        ) // long类型转成Date类型
        return dateToString(date, formatType)
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun stringToDate(strTime: String, formatType: String): Date? {
        val formatter = SimpleDateFormat(formatType)
        var date: Date? = null
        date = formatter.parse(strTime)
        return date
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    @Throws(ParseException::class)
    fun longToDate(currentTime: Long, formatType: String): Date? {
        val dateOld = Date(currentTime) // 根据long类型的毫秒数生命一个date类型的时间
        val sDateTime = dateToString(
            dateOld,
            formatType
        ) // 把date类型的时间转换为string
        return stringToDate(
            sDateTime,
            formatType
        )
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    @Throws(ParseException::class)
    fun stringToLong(strTime: String, formatType: String): Long {
        val date = stringToDate(
            strTime,
            formatType
        ) // String类型转成date类型
        return if (date == null) {
            0
        } else {
            dateToLong(date)
        }


    }

    // date要转换的date类型的时间
    fun dateToLong(date: Date): Long {
        return date.time
    }

}

