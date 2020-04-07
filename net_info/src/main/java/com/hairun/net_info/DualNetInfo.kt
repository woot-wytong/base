package com.hairun.net_info

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.Q
import android.telephony.*
import android.util.Log
import cn.bfy.dualsim.DualsimBase
import cn.bfy.dualsim.TelephonyManagement
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ReflectUtils
import com.blankj.utilcode.util.Utils
import com.hairun.net_info.NetInfoBean.TAG
import org.json.JSONObject


object DualNetInfo {
    private var telephonyManager: TelephonyManager? = null
    private var manager: SubscriptionManager? = null
    private var telephonyInfo: TelephonyManagement.TelephonyInfo? = null

    private var sim1StateListener: DualSimSignalStrengthsListener? = null
    private var sim2StateListener: DualSimSignalStrengthsListener? = null

    private fun strSimOperator(operatorCode: String): String {
        var operator = "未知SIM卡运营商"
        when (operatorCode) {
            "46000", "46002", "46007" -> operator = "移动"
            "46003", "46005", "46011" -> operator = "电信"
            "46001", "46006" -> operator = "联通"
            "46020" -> operator = "铁通"
        }
        return operator
    }


    @JvmStatic
    fun getSimInfo() {//android 9 以下适用
        if (telephonyInfo == null) {
            telephonyInfo = TelephonyManagement.getInstance()
                .updateTelephonyInfo(Utils.getApp())
                .getTelephonyInfo(Utils.getApp())
        }

        NetInfoBean.setMainSim(telephonyInfo!!.defaultDataSlotId)

        if (telephonyInfo!!.isDualSIM) {
            NetInfoBean.setSimState(NetInfoBean.DUAL_SIM)
            NetInfoBean.setSubId(telephonyInfo!!.subIdSIM1)
            NetInfoBean.setSimOperator(strSimOperator(telephonyInfo!!.operatorSIM1))
            NetInfoBean.setSubId2(telephonyInfo!!.subIdSIM2)
            NetInfoBean.setSimOperator2(strSimOperator(telephonyInfo!!.operatorSIM2))
        } else {
            when (telephonyInfo!!.slotIdSIM1) {
                DualsimBase.TYPE_SIM_MAIN -> {
                    NetInfoBean.setSimState(NetInfoBean.SIM_ONE)
                    NetInfoBean.setSubId(telephonyInfo!!.subIdSIM1)
                    NetInfoBean.setSimOperator(strSimOperator(telephonyInfo!!.operatorSIM1))
                }
                DualsimBase.TYPE_SIM_ASSISTANT -> {
                    NetInfoBean.setSimState(NetInfoBean.SIM_TWO)
                    NetInfoBean.setSubId2(telephonyInfo!!.subIdSIM2)
                    NetInfoBean.setSimOperator2(strSimOperator(telephonyInfo!!.operatorSIM2))
                }
                else -> {
                    NetInfoBean.setSimState(NetInfoBean.NO_SIM)
                }
            }
        }
    }

    @SuppressLint("MissingPermission", "NewApi")
    @JvmStatic
    fun getSimInfoQ() {   //Android 10 适用

        val subId = NetInfo.getDefalutDataID()
        val subscriptionManager =
            Utils.getApp().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        val info =
            subscriptionManager.getActiveSubscriptionInfo(subId)
        Log.d(TAG, "主卡卡槽------------------" + info.simSlotIndex)
        NetInfoBean.setMainSim(info.simSlotIndex)


        val info1 =
            subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0)
        val info2 =
            subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1)
        if (info1 != null && info2 != null) {
            NetInfoBean.setSimState(NetInfoBean.DUAL_SIM)
            NetInfoBean.setSubId(info1.cardId)
            NetInfoBean.setSimOperator(strSimOperator(info1.mccString + info1.mncString))
            NetInfoBean.setSubId2(info2.cardId)
            NetInfoBean.setSimOperator2(strSimOperator(info2.mccString + info2.mncString))
        } else if (info1 != null) {
            NetInfoBean.setSimState(NetInfoBean.SIM_ONE)
            NetInfoBean.setSubId(info1.cardId)
            NetInfoBean.setSimOperator(strSimOperator(info1.mccString + info1.mncString))
        } else if (info2 != null) {
            NetInfoBean.setSimState(NetInfoBean.SIM_TWO)
            NetInfoBean.setSubId2(info2.cardId)
            NetInfoBean.setSimOperator2(strSimOperator(info2.mccString + info2.mncString))
        } else {
            NetInfoBean.setSimState(NetInfoBean.NO_SIM)
        }
    }

    private fun getRegisteredCellInfo(cellInfos: MutableList<CellInfo>): ArrayList<CellInfo> {
        val registeredCellInfos = ArrayList<CellInfo>()
        if (cellInfos.isNotEmpty()) {
            for (i in cellInfos.indices) {
                if (cellInfos[i].isRegistered) {
                    registeredCellInfos.add(cellInfos[i])
                }
            }
        }
        return registeredCellInfos
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    @JvmStatic
    fun getDualSignal() {  //双卡信号
        if (telephonyManager == null) {
            telephonyManager =
                Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        }


        when (NetInfoBean.getSimState()) {
            NetInfoBean.SIM_ONE -> {
                sim1StateListener = DualSimSignalStrengthsListener(NetInfoBean.subId)
                telephonyManager!!.listen(
                    sim1StateListener,
                    PhoneStateListener.LISTEN_CELL_LOCATION or PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                )
            }
            NetInfoBean.SIM_TWO -> {
                sim2StateListener = DualSimSignalStrengthsListener(NetInfoBean.subId2)
                telephonyManager!!.listen(
                    sim2StateListener,
                    PhoneStateListener.LISTEN_CELL_LOCATION or PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                )
            }
            NetInfoBean.DUAL_SIM -> {
                Log.d(TAG, "sim1:${NetInfoBean.subId}---sim2:${NetInfoBean.subId2}")
                sim1StateListener = DualSimSignalStrengthsListener(NetInfoBean.subId)
                sim2StateListener = DualSimSignalStrengthsListener(NetInfoBean.subId2)
                telephonyManager!!.listen(
                    sim1StateListener,
                    PhoneStateListener.LISTEN_CELL_LOCATION or PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                )

                telephonyManager!!.listen(
                    sim2StateListener,
                    PhoneStateListener.LISTEN_CELL_LOCATION or PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                )
            }
        }
    }

    @JvmStatic
    fun stopGetDualSignal() {
        when (NetInfoBean.getSimState()) {
            NetInfoBean.SIM_ONE -> {
                telephonyManager!!.listen(
                    sim1StateListener,
                    PhoneStateListener.LISTEN_NONE
                )
            }
            NetInfoBean.SIM_TWO -> {
                telephonyManager!!.listen(
                    sim2StateListener,
                    PhoneStateListener.LISTEN_NONE
                )
            }
            NetInfoBean.DUAL_SIM -> {
                telephonyManager!!.listen(
                    sim1StateListener,
                    PhoneStateListener.LISTEN_NONE
                )
                telephonyManager!!.listen(
                    sim2StateListener,
                    PhoneStateListener.LISTEN_NONE
                )
            }
        }

    }


    @SuppressLint("MissingPermission", "ObsoleteSdkInt")
    @JvmStatic
    fun getDualCellInfo() { //双卡
        var lac1 = -1
        var cellId1 = -1
        var pci1 = -1

        var lac2 = -1
        var cellId2 = -1
        var pci2 = -1

        if (SDK_INT >= VERSION_CODES.M) {
            if (manager == null || telephonyManager == null) {
                manager =
                    Utils.getApp().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                telephonyManager =
                    Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            }
            if (telephonyManager!!.allCellInfo != null) {
                val allCellinfo = telephonyManager!!.allCellInfo
                val activeSubscriptionInfoList = manager!!.activeSubscriptionInfoList
                val regCellInfo = getRegisteredCellInfo(allCellinfo)

                activeSubscriptionInfoList.forEachIndexed { _, subs ->
                    if (activeSubscriptionInfoList.size >= 2) {
                        if (regCellInfo.size >= 2) {
                            if (subs.simSlotIndex == 0) {
                                if (subs.carrierName != "No service") {
                                    when (val info1 = regCellInfo[0]) {
                                        is CellInfoLte -> {
                                            val cellIdentityLte = info1.cellIdentity

                                            Log.d(TAG,"cellIdentityLte------------------"+GsonUtils.toJson(cellIdentityLte))

                                            if (cellIdentityLte.tac != 2147483647) lac1 =
                                                cellIdentityLte.tac
                                            if (cellIdentityLte.ci != 2147483647) cellId1 =
                                                cellIdentityLte.ci
                                            if (cellIdentityLte.pci != 2147483647) pci1 =
                                                cellIdentityLte.pci
                                        }
                                        is CellInfoGsm -> {
                                            val cellIdentityGsm = info1.cellIdentity
                                            if (cellIdentityGsm.lac != 2147483647) lac1 =
                                                cellIdentityGsm.lac
                                            if (cellIdentityGsm.cid != 2147483647) cellId1 =
                                                cellIdentityGsm.cid
                                        }
                                        is CellInfoCdma -> {
                                            val cellIdentityCdma = info1.cellIdentity
                                            //2147483647是一个无效值  直接跳过
                                            if (cellIdentityCdma.networkId != 2147483647) lac1 =
                                                cellIdentityCdma.networkId
                                            if (cellIdentityCdma.basestationId != 2147483647) cellId1 =
                                                cellIdentityCdma.basestationId
                                        }
                                        is CellInfoWcdma -> {
                                            val cellIdentityWcdma: CellIdentityWcdma?
                                            if (SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                                                cellIdentityWcdma = info1.cellIdentity
                                                if (cellIdentityWcdma.lac != 2147483647) lac1 =
                                                    cellIdentityWcdma.lac
                                                if (cellIdentityWcdma.cid != 2147483647) cellId1 =
                                                    cellIdentityWcdma.cid

                                            }
                                        }
                                    }
                                }
                            } else if (subs.simSlotIndex == 1) {
                                if (subs.carrierName != "No service") {
                                    when (val info2 = regCellInfo[1]) {
                                        is CellInfoLte -> {
                                            val cellIdentityLte = info2.cellIdentity
                                            if (cellIdentityLte.tac != 2147483647) lac2 =
                                                cellIdentityLte.tac
                                            if (cellIdentityLte.ci != 2147483647) cellId2 =
                                                cellIdentityLte.ci
                                            if (cellIdentityLte.pci != 2147483647) pci2 =
                                                cellIdentityLte.pci
                                        }
                                        is CellInfoGsm -> {
                                            val cellIdentityGsm = info2.cellIdentity
                                            if (cellIdentityGsm.lac != 2147483647) lac2 =
                                                cellIdentityGsm.lac
                                            if (cellIdentityGsm.cid != 2147483647) cellId2 =
                                                cellIdentityGsm.cid
                                        }
                                        is CellInfoCdma -> {
                                            val cellIdentityCdma = info2.cellIdentity
                                            if (cellIdentityCdma.networkId != 2147483647) lac2 =
                                                cellIdentityCdma.networkId
                                            if (cellIdentityCdma.basestationId != 2147483647) cellId2 =
                                                cellIdentityCdma.basestationId
                                        }
                                        is CellInfoWcdma -> {
                                            val cellIdentityWcdma: CellIdentityWcdma?
                                            if (SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                                                cellIdentityWcdma = info2.cellIdentity
                                                if (cellIdentityWcdma.lac != 2147483647) lac2 =
                                                    cellIdentityWcdma.lac
                                                if (cellIdentityWcdma.cid != 2147483647) cellId2 =
                                                    cellIdentityWcdma.cid
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (activeSubscriptionInfoList.size == 1) {
                        if (regCellInfo.size >= 1) {
                            if (subs.simSlotIndex == 0) {
                                if (subs.carrierName != "No service") {
                                    when (val info1 = regCellInfo[0]) {
                                        is CellInfoLte -> {
                                            val cellIdentityLte = info1.cellIdentity
                                            if (cellIdentityLte.tac != 2147483647) lac1 =
                                                cellIdentityLte.tac
                                            if (cellIdentityLte.ci != 2147483647) cellId1 =
                                                cellIdentityLte.ci
                                            if (cellIdentityLte.pci != 2147483647) pci1 =
                                                cellIdentityLte.pci
                                        }
                                        is CellInfoGsm -> {
                                            val cellIdentityGsm = info1.cellIdentity
                                            if (cellIdentityGsm.lac != 2147483647) lac1 =
                                                cellIdentityGsm.lac
                                            if (cellIdentityGsm.cid != 2147483647) cellId1 =
                                                cellIdentityGsm.cid
                                        }
                                        is CellInfoCdma -> {
                                            val cellIdentityCdma = info1.cellIdentity
                                            if (cellIdentityCdma.networkId != 2147483647) lac2 =
                                                cellIdentityCdma.networkId
                                            if (cellIdentityCdma.basestationId != 2147483647) cellId2 =
                                                cellIdentityCdma.basestationId
                                        }
                                        is CellInfoWcdma -> {
                                            val cellIdentityWcdma: CellIdentityWcdma?
                                            if (SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                                                cellIdentityWcdma = info1.cellIdentity
                                                if (cellIdentityWcdma.lac != 2147483647) lac2 =
                                                    cellIdentityWcdma.lac
                                                if (cellIdentityWcdma.cid != 2147483647) cellId2 =
                                                    cellIdentityWcdma.cid
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        if (lac1 != -1) {
            NetInfoBean.setTac(lac1.toString())
        }
        if (cellId1 != -1) {
            NetInfoBean.setEci(cellId1.toString() + "(" + (cellId1 / 256).toString() + "-" + (cellId1 % 256).toString() + ")")
        }
        if (pci1 != -1) {
            NetInfoBean.setPci(pci1.toString())
        }


        if (lac2 != -1) {
            NetInfoBean.setTac2(lac2.toString())
        }
        if (cellId1 != -1) {
            NetInfoBean.setEci2(cellId2.toString() + "(" + (cellId2 / 256).toString() + "-" + (cellId2 % 256).toString() + ")")
        }
        if (pci2 != -1) {
            NetInfoBean.setPci2(pci2.toString())
        }

        Log.d(
            "wyt_sim1_sim2", "\n simInfo" +
                    "\n lac \t\t sim1 \t\t $lac1 \t\t\t sim2 \t\t $lac2" +
                    "\n cid \t\t sim1 \t\t $cellId1 \t\t\t sim2 \t\t $cellId2" +
                    "\n pci \t\t sim1 \t\t $pci1 \t\t\t sim2 \t\t $pci2"
        )
    }

    private class DualSimSignalStrengthsListener(val subId: Int) : PhoneStateListener() {
        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
            super.onSignalStrengthsChanged(signalStrength)
            if (SDK_INT >= Q) {
                getSignalQ(signalStrength, subId)
            } else {
                getSignal(signalStrength, subId)
            }

        }

        init {
            if (SDK_INT >= Q) {

            } else {
                ReflectUtil.setFieldValue(this, "mSubId", subId)
            }

        }
    }


    private fun getSignal(signalStrength: SignalStrength, subId: Int) { //android 9以下
        var rsrp = "--"
        var rsrq = "--"
        var sinr = "--"

        val netSystem = NetInfo.getNetWorkIs234G()
        if (netSystem == "4G" || netSystem == "4G+") {
            val o1 = ReflectUtils.reflect(signalStrength).method("getLteRsrp").get<Any>()
            val o2 = ReflectUtils.reflect(signalStrength).method("getLteRsrq").get<Any>()
            val o3 = ReflectUtils.reflect(signalStrength).method("getLteRssnr").get<Any>()
            if (o1.toString() != "2147483647" &&
                o2.toString() != "2147483647" &&
                o3.toString() != "2147483647"
            ) {
                rsrp = o1.toString()
                rsrq = o2.toString()
                sinr =
                    YskMath.div(o3.toString().toInt().toDouble(), 10.0, 1).toString()
            }
        } else if (netSystem == "3G") {
            val o1 = ReflectUtils.reflect(signalStrength).method("getCdmaDbm").get<Any>()
            if (o1.toString() != "2147483647") {
                rsrp = o1.toString()
            }
        }
        if (netSystem == "2G") {
            val o1 = ReflectUtils.reflect(signalStrength).method("getGsmDbm").get<Any>()
            if (o1.toString() != "2147483647") {
                rsrp = o1.toString()
            }
        }
        if (NetInfoBean.subId == subId) {

            Log.d(
                TAG, "\n simInfo" +
                        "\n rsrp \t\t sim1 \t\t $rsrp" +
                        "\n rsrq \t\t sim1 \t\t $rsrq" +
                        "\n sinr \t\t sim1 \t\t $sinr"
            )

            if (rsrp != "--") NetInfoBean.setRsrp(rsrp)
            if (rsrq != "--") NetInfoBean.setRsrq(rsrq)
            if (sinr != "--") NetInfoBean.setSinr(sinr)
        } else if (NetInfoBean.subId2 == subId) {
            Log.d(
                TAG, "\n simInfo" +
                        "\n rsrp \t\t sim2 \t\t $rsrp" +
                        "\n rsrq \t\t sim2 \t\t $rsrq" +
                        "\n sinr \t\t sim2 \t\t $sinr"
            )

            if (rsrp != "--") NetInfoBean.setRsrp2(rsrp)
            if (rsrq != "--") NetInfoBean.setRsrq2(rsrq)
            if (sinr != "--") NetInfoBean.setSinr2(sinr)
        }

    }


    @SuppressLint("NewApi")
    private fun getSignalQ(signalStrength: SignalStrength, subId: Int) { //android 10
        if (NetInfoBean.simState == NetInfoBean.UNKNOWN_STATE) {
            NetInfoBean.getSimState()
        }

        Log.d(TAG,GsonUtils.toJson(signalStrength))

        val phoneId = JSONObject(GsonUtils.toJson(signalStrength)).optString("mPhoneId")
        var rsrp = "--"
        var rsrq = "--"
        var sinr = "--"
        val cellSignalStrengths = signalStrength.cellSignalStrengths
        for (cellSignalStrength in cellSignalStrengths) {
            if (cellSignalStrength is CellSignalStrengthLte) {
                rsrp = cellSignalStrength.rsrp.toString()
                rsrq = cellSignalStrength.rsrq.toString()
                val rssnr = cellSignalStrength.rssnr
                if (rssnr != 2147483647) {
                    sinr = YskMath.div(cellSignalStrength.rssnr.toDouble(), 10.0, 1)
                        .toString()
                }
            } else if (cellSignalStrength is CellSignalStrengthCdma) {
                rsrp = cellSignalStrength.cdmaDbm.toString()
            } else if (cellSignalStrength is CellSignalStrengthWcdma) {
                rsrp = cellSignalStrength.getDbm().toString()
            } else if (cellSignalStrength is CellSignalStrengthGsm) {
                rsrp = cellSignalStrength.getDbm().toString()
            }
        }

        if (phoneId != "") {
            when(phoneId){
                "0"->{
                    Log.d(
                        TAG, "\n simInfo" +
                                "\n rsrp \t\t sim1 \t\t $rsrp" +
                                "\n rsrq \t\t sim1 \t\t $rsrq" +
                                "\n sinr \t\t sim1 \t\t $sinr"
                    )

                    if (rsrp != "--") NetInfoBean.setRsrp(rsrp)
                    if (rsrq != "--") NetInfoBean.setRsrq(rsrq)
                    if (sinr != "--") NetInfoBean.setSinr(sinr)
                }
                "1"->{
                    Log.d(
                        TAG, "\n simInfo" +
                                "\n rsrp \t\t sim2 \t\t $rsrp" +
                                "\n rsrq \t\t sim2 \t\t $rsrq" +
                                "\n sinr \t\t sim2 \t\t $sinr"
                    )

                    if (rsrp != "--") NetInfoBean.setRsrp2(rsrp)
                    if (rsrq != "--") NetInfoBean.setRsrq2(rsrq)
                    if (sinr != "--") NetInfoBean.setSinr2(sinr)
                }
            }
        }else if (NetInfoBean.subId == subId) {
            Log.d(
                TAG, "\n simInfo" +
                        "\n rsrp \t\t sim1 \t\t $rsrp" +
                        "\n rsrq \t\t sim1 \t\t $rsrq" +
                        "\n sinr \t\t sim1 \t\t $sinr"
            )

            if (rsrp != "--") NetInfoBean.setRsrp(rsrp)
            if (rsrq != "--") NetInfoBean.setRsrq(rsrq)
            if (sinr != "--") NetInfoBean.setSinr(sinr)
        } else if (NetInfoBean.subId2 == subId) {
            Log.d(
                TAG, "\n simInfo" +
                        "\n rsrp \t\t sim2 \t\t $rsrp" +
                        "\n rsrq \t\t sim2 \t\t $rsrq" +
                        "\n sinr \t\t sim2 \t\t $sinr"
            )

            if (rsrp != "--") NetInfoBean.setRsrp2(rsrp)
            if (rsrq != "--") NetInfoBean.setRsrq2(rsrq)
            if (sinr != "--") NetInfoBean.setSinr2(sinr)
        }


    }


}