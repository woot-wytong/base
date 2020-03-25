package com.wyt.demo.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.wyt.demo.R
import com.wyt.woot_base.activity.BaseActivity
import ir.siaray.telephonymanagerplus.TelephonyManagerPlus

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
            val telephonyManagerPlus = TelephonyManagerPlus.getInstance(this)
            val imei1 = telephonyManagerPlus.imei1
            val imei2 = telephonyManagerPlus.imei2
            val simOperatorCode1 = telephonyManagerPlus.simOperatorCode1
            val simOperatorCode2 = telephonyManagerPlus.simOperatorCode2
            val simOperatorName1 = telephonyManagerPlus.simOperatorName1
            val simOperatorName2 = telephonyManagerPlus.simOperatorName2
            val simSerialNumber1 = telephonyManagerPlus.simSerialNumber1
            val simSerialNumber2 = telephonyManagerPlus.simSerialNumber2
            val subscriberId1 = telephonyManagerPlus.subscriberId1
            val subscriberId2 = telephonyManagerPlus.subscriberId2
            val mcc1 = telephonyManagerPlus.mcc1
            val mcc2 = telephonyManagerPlus.mcc2
            val mnc1 = telephonyManagerPlus.mnc1
            val mnc2 = telephonyManagerPlus.mnc2
            val cid1 = telephonyManagerPlus.cid1
            val cid2 = telephonyManagerPlus.cid2
            val lac1 = telephonyManagerPlus.lac1
            val lac2 = telephonyManagerPlus.lac2


            Log.d(
                "wyt_test",
                "--\n" +
                        "imei1:\t\t\t" + imei1 + "\n" +
                        "imei2:\t\t\t" + imei2 + "\n" +
                        "simOperatorCode1:\t\t\t" + simOperatorCode1 + "\n" +
                        "simOperatorCode2:\t\t\t" + simOperatorCode2 + "\n" +
                        "simOperatorName1:\t\t\t" + simOperatorName1 + "\n" +
                        "simOperatorName2:\t\t\t" + simOperatorName2 + "\n" +
                        "simSerialNumber1:\t\t\t" + simSerialNumber1 + "\n" +
                        "simSerialNumber2:\t\t\t" + simSerialNumber2 + "\n" +
                        "subscriberId1:\t\t\t" + subscriberId1 + "\n" +
                        "subscriberId2:\t\t\t" + subscriberId2 + "\n" +
                        "mcc1:\t\t\t" + mcc1 + "\n" +
                        "mcc2:\t\t\t" + mcc2 + "\n" +
                        "mnc1:\t\t\t" + mnc1 + "\n" +
                        "mnc2:\t\t\t" + mnc2 + "\n" +
                        "cid1:\t\t\t" + cid1 + "\n" +
                        "cid2:\t\t\t" + cid2 + "\n" +
                        "lac1:\t\t\t" + lac1 + "\n" +
                        "lac2:\t\t\t" + lac2 + "\n"
            )
        }


    }
}
