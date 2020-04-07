package com.wyt.demo.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.Log
import cn.bfy.dualsim.TelephonyManagement
import com.blankj.utilcode.util.Utils
import com.hairun.net_info.DualNetInfo
import com.hairun.net_info.NetInfoBean
import com.wyt.demo.R
import com.wyt.woot_base.activity.BaseActivity
import java.util.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                100
            )
        } else {
//            NetInfoBean.startListener(true)
//            val handler = Handler()
//            val runnable = object :Runnable{
//                override fun run() {
//                    Log.d(
//                        "wyt_sssss", "-------------------------------------------------\n"
//                                + "SimOperator:\t\t\t ${NetInfoBean.getSimInfo1()}" + "\n"
//                                + "SimOperator2:\t\t\t ${NetInfoBean.getSimInfo2()}" + "\n"
//                                + "Eci:\t\t\t ${NetInfoBean.getEci()}" + "\n"
//                                + "Eci2:\t\t\t ${NetInfoBean.getEci2()}" + "\n"
//                                + "getTac:\t\t\t ${NetInfoBean.getTac()}" + "\n"
//                                + "getTac2:\t\t\t ${NetInfoBean.getTac2()}" + "\n"
//                                + "getRsrp:\t\t\t ${NetInfoBean.getRsrp()}" + "\n"
//                                + "getRsrp2:\t\t\t ${NetInfoBean.getRsrp2()}" + "\n"
//                                + "getRsrq:\t\t\t ${NetInfoBean.getRsrq()}" + "\n"
//                                + "getRsrq2:\t\t\t ${NetInfoBean.getRsrq2()}" + "\n"
//                                + "getSinr:\t\t\t ${NetInfoBean.getSinr()}" + "\n"
//                                + "getSinr2:\t\t\t ${NetInfoBean.getSinr2()}" + "\n"
//                                + "getNetWorkOperator:\t\t\t ${NetInfoBean.getNetWorkOperator()}" + "\n"
//                                + "getNetWorkType:\t\t\t ${NetInfoBean.getNetWorkType()}" + "\n"
//                    )
//                    handler.postDelayed(this,3000)
//                }
//            }
//            handler.post(runnable)

           val telephonyInfo = TelephonyManagement.getInstance()
                .updateTelephonyInfo(Utils.getApp())
                .getTelephonyInfo(Utils.getApp())


            Log.d("wyt-----",telephonyInfo.toString())

        }


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        NetInfoBean.stopListener()
    }
}
