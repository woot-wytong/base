package com.hairun.net_info;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.NetworkUtils;

import java.util.Timer;
import java.util.TimerTask;

public class NetInfoBean {

    private static boolean isRun = false;

    static final String TAG = "wyt_sim_card";
    public static final int UNKNOWN_STATE = -1;         //未知sim卡状态
    public static final int NO_SIM = 0;                 //无卡
    public static final int SIM_ONE = 1;                //单卡，卡1
    public static final int SIM_TWO = 2;                //单卡，卡2
    public static final int DUAL_SIM = 3;               //双卡

    private static Boolean moreAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    static int simState = UNKNOWN_STATE;

    private static String netWorkType = "未知网络类型";              // 当前网络类型    2G,3G,4G,WIFI
    private static String netIp = "";                               //当前使用网络IP
    private static String netWorkOperator = "未知网络运营商";       //当前使用网络运营商  电信移动和联通铁通
    private static int mainSim = -1; //主卡  0,1 当前流量卡

    private static String simOperator = "未知SIM卡运营商";            // sim1卡运营商 电信移动和联通
    static int subId = 0x99;
    private static String Tac = "--";       //卡1
    private static String Eci = "--";       //卡1
    private static String Rsrp = "--";      //卡1
    private static String Rsrq = "--";      //卡1
    private static String Sinr = "--";      //卡1
    private static String Pci = "--";

    private static String simOperator2 = "未知SIM卡运营商";// sim2卡运营商 电信移动和联通
    static int subId2 = 0x99;
    private static String Tac2 = "--";       //卡2
    private static String Eci2 = "--";       //卡2
    private static String Rsrp2 = "--";     //卡2
    private static String Rsrq2 = "--";     //卡2
    private static String Sinr2 = "--";     //卡2
    private static String Pci2 = "--";

    private static String netDownSpeed = "";//当前实时下载网速
    private static String netUpSpeed = "";//当前实时上传网速

    private static long netWorkOperatorStamp = 0;

    public static void setTac(String tac) {
        Tac = tac;
    }

    public static void setEci(String eci) {
        Eci = eci;
    }

    public static void setRsrp(String rsrp) {
        Rsrp = rsrp;
    }

    public static void setRsrq(String rsrq) {
        Rsrq = rsrq;
    }

    public static void setSinr(String sinr) {
        Sinr = sinr;
    }

    public static void setSimOperator(String simOperator) {
        NetInfoBean.simOperator = simOperator;
    }

    static void setNetWorkType(String netWorkType) {
        NetInfoBean.netWorkType = netWorkType;
    }

    static void setNetIp(String netIp) {
        NetInfoBean.netIp = netIp;
    }

    public static void setSimOperator2(String simOperator2) {
        NetInfoBean.simOperator2 = simOperator2;
    }

    public static void setMainSim(int mainSim) {
        NetInfoBean.mainSim = mainSim;
    }

    public static void setTac2(String tac2) {
        Tac2 = tac2;
    }

    public static void setEci2(String eci2) {
        Eci2 = eci2;
    }

    public static void setRsrp2(String rsrp2) {
        Rsrp2 = rsrp2;
    }

    public static void setRsrq2(String rsrq2) {
        Rsrq2 = rsrq2;
    }

    public static void setSinr2(String sinr2) {
        Sinr2 = sinr2;
    }

    public static void setSubId(int subId) {
        NetInfoBean.subId = subId;
    }

    public static void setSubId2(int subId2) {
        NetInfoBean.subId2 = subId2;
    }

    public static String getPci() {
        return Pci;
    }

    public static void setPci(String pci) {
        Pci = pci;
    }

    public static String getPci2() {
        return Pci2;
    }

    public static void setPci2(String pci2) {
        Pci2 = pci2;
    }

    static void setNetWorkOperator(String netWorkOperator) {
        NetInfoBean.netWorkOperator = netWorkOperator;
    }

    static void setSimState(int simState) {
        NetInfoBean.simState = simState;
    }

    private static void getSimInfo(){
        if (moreAndroidQ) {
            DualNetInfo.getSimInfoQ();
        } else {
            DualNetInfo.getSimInfo();
        }
    }

    public static int getSimState() {
        return simState;
    }


    /**
     * @return 主卡，当前流量卡soltIndex（卡槽）  0/1
     */
    public static int getMainSim() {
        return mainSim;
    }

    /**
     * @return 卡1运营商
     */
    public static String getSimInfo1() {
        return simOperator;
    }

    /**
     * @return 卡2运营商
     */
    public static String getSimInfo2() {
        return simOperator2;
    }

    /**
     * @return 当前Sim卡网络类型
     */
    public static String getNetWorkType() {
        return NetInfo.getNetWorkIs234G();
    }

    /**
     * @return 获取当前使用网络的Ip
     */
    public static String getNetIp() {
        NetInfo.getWiFiOperator();
        return netIp;
    }

    /**
     * @return 当前网络类型
     */
    public static String getNetWorkTypeOrWiFi() {
        NetInfo.getNetWorkIs234GOrWiFi();
        return netWorkType;
    }

    /**
     * @return 当前使用网络的运营商
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getNetWorkOperator() {
        //是wifi是通过获取当前网络IP拿到网络归属运营商
        //不是wifi直接拿sim卡运营商
        if (NetworkUtils.isWifiConnected()) {
            long thisTime = System.currentTimeMillis();
            if (thisTime - netWorkOperatorStamp > 3000) {
                NetInfo.getWiFiOperator();
                netWorkOperatorStamp = thisTime;
            }
        } else {
            netWorkOperator = getSimInfo1();
        }
        return netWorkOperator;
    }

    public static String getTac() {
        return Tac;
    }

    public static String getEci() {
        return Eci;
    }

    public static String getRsrp() {
        return Rsrp;
    }

    public static String getRsrq() {
        return Rsrq;
    }

    public static String getSinr() {
        return Sinr;
    }

    public static String getTac2() {
        return Tac2;
    }

    public static String getEci2() {
        return Eci2;
    }

    public static String getRsrp2() {
        return Rsrp2;
    }

    public static String getRsrq2() {
        return Rsrq2;
    }

    public static String getSinr2() {
        return Sinr2;
    }

    static void setNetDownSpeed(String netDownSpeed) {
        NetInfoBean.netDownSpeed = netDownSpeed;
    }

    static void setNetUpSpeed(String netUpSpeed) {
        NetInfoBean.netUpSpeed = netUpSpeed;
    }

    public static String getNetDownSpeed() {
        return netDownSpeed;
    }

    public static String getNetUpSpeed() {
        return netUpSpeed;
    }


    private static Timer timer;

    public static void startListener(final boolean isCell){
        if(isRun){
            return;
        }
        getSimInfo();
        DualNetInfo.getDualSignal();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getSimInfo();
               if(isCell){
                   if (simState == DUAL_SIM) {  //双卡状态
                       DualNetInfo.getDualCellInfo();
                   } else if (simState == SIM_ONE
                           || simState == SIM_TWO
                   ) {                          //单卡状态
                       NetInfo.getCellInfo();
                   }
               }
            }
        },500,3000);
        isRun = true;
    }

    public static void stopListener(){
        if(isRun){
            timer.purge();
            timer.cancel();
            DualNetInfo.stopGetDualSignal();
            isRun = false;
        }

    }
}
