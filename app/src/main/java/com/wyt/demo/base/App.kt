package com.wyt.demo.base

import android.app.Application
import android.os.Environment
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.zhouyou.http.EasyHttp
/**
 * Application
 * 2019.11.28 ->wyt
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        MMKV.initialize(this)
        QMUISwipeBackActivityManager.init(this)
        initEasyHttp()
        initBugly()
    }

    private fun initBugly() {
        Beta.autoInit = true //自动监测
        Beta.storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)  //存放地址
        Beta.showInterruptedStrategy = true //下次打开自动弹出
        Beta.enableNotification = true
        Bugly.init(applicationContext, "5585671c9a", false)
    }

    private fun initEasyHttp() {
        EasyHttp.init(this)
        EasyHttp.getInstance()
            // 打开该调试开关并设置TAG,不需要就不要加入该行
            // 最后的true表示是否打印内部异常，一般打开方便调试错误
            .debug("EasyHttp", true)
            //如果使用默认的60秒,以下三行也不需要设置
            .setReadTimeOut(60 * 1000)
            .setWriteTimeOut(60 * 100)
            .setConnectTimeout(60 * 100)
            //可以全局统一设置超时重连次数,默认为3次,那么最差的情况会请求4次(一次原始请求,三次重连请求),
            //不需要可以设置为0
            .setRetryCount(0)
            //可以全局统一设置超时重试间隔时间,默认为500ms,不需要可以设置为0
            .setRetryDelay(0)
            //可以全局统一设置超时重试间隔叠加时间,默认为0ms不叠加
            .setRetryIncreaseDelay(0)
    }
}