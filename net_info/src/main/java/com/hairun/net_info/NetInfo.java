package com.hairun.net_info;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.Q;
import static com.hairun.net_info.NetInfoBean.TAG;

class NetInfo {
    private boolean isRun;

    static void getNetWorkIs234GOrWiFi() {
        String netWorkName = "未知网络类型";
        if (NetworkUtils.isWifiConnected()) {
            NetInfoBean.setNetWorkType("WIFI");
            return;
        }
        TelephonyManager mTelephonyManager = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkType 返回一个常数，表示目前在设备上使用的无线电技术（网络类型）
        assert mTelephonyManager != null;
        switch (mTelephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS://1
            case TelephonyManager.NETWORK_TYPE_EDGE://2
            case TelephonyManager.NETWORK_TYPE_CDMA://4
            case TelephonyManager.NETWORK_TYPE_1xRTT://7
            case TelephonyManager.NETWORK_TYPE_IDEN: //11
            case TelephonyManager.NETWORK_TYPE_GSM:  //16
                netWorkName = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS://3
            case TelephonyManager.NETWORK_TYPE_EVDO_0://5
            case TelephonyManager.NETWORK_TYPE_EVDO_A://6
            case TelephonyManager.NETWORK_TYPE_HSDPA://8
            case TelephonyManager.NETWORK_TYPE_HSUPA://9
            case TelephonyManager.NETWORK_TYPE_HSPA://10
            case TelephonyManager.NETWORK_TYPE_EVDO_B: //12
            case TelephonyManager.NETWORK_TYPE_EHRPD:  //14
            case TelephonyManager.NETWORK_TYPE_HSPAP:  //15
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:  //17
                netWorkName = "3G";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE://13
            case TelephonyManager.NETWORK_TYPE_IWLAN://18
                netWorkName = "4G";
                break;
            case 19:
            case 139://魅族X6 4G+ type==139
                netWorkName = "4G+";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN://0
                netWorkName = "未知网络类型";
                break;
        }
        NetInfoBean.setNetWorkType(netWorkName);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    static int getDefalutDataID() {
        SubscriptionManager subscriptionManager = (SubscriptionManager) Utils.getApp().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        int subscriberId = 0;
        if (Build.VERSION.SDK_INT > 24) {
            subscriberId = SubscriptionManager.getDefaultSubscriptionId();
        } else {
            try {
                Class cls = SubscriptionManager.class;
                Method method = cls.getDeclaredMethod("getDefaultDataSubId");
                subscriberId = (Integer) method.invoke(subscriptionManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subscriberId;
    }

    @SuppressLint("Recycle")
    public static void getSimInfo() { //Android 9 以下适用
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cursor = null;
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        cursor = contentResolver.query(uri,
                new String[]{"_id", "icc_id", "sim_id", "display_name", "carrier_name", "name_source", "color", "number", "display_number_format", "data_roaming", "mcc", "mnc"}, "0=0",
                new String[]{}, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                String icc_id = cursor.getString(cursor.getColumnIndex("icc_id"));
                String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
                int sim_id = cursor.getInt(cursor.getColumnIndex("sim_id"));
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                int mcc = cursor.getInt(cursor.getColumnIndex("mcc"));
                int mnc = cursor.getInt(cursor.getColumnIndex("mnc"));

                Log.d("Q_M", "_id->" + _id);
                Log.d("Q_M", "icc_id-->" + icc_id);
                Log.d("Q_M", "sim_id-->" + sim_id);
                Log.d("Q_M", "display_name-->" + display_name);
                Log.d("Q_M", "mcc->" + mcc);
                Log.d("Q_M", "mnc->" + mnc);
                Log.d("Q_M", "-------------------------------------------------------------");
            }
        }
    }

    public static void getWiFiOperator() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://pv.sohu.com/cityjson").build();
                Call call = client.newCall(request);
                try {
                    //同步get请求,一定开启子线程，不能再主线程操作。
                    Response response = call.execute();
                    String s = Objects.requireNonNull(response.body()).string();
                    s = s.split("=")[1];
                    s = s.replaceAll(";", "");
                    JSONObject jo = new JSONObject(s);
                    String ip = jo.optString("cip");
                    NetInfoBean.setNetIp(ip);
                    try {
                        String area = Ip2Region.cultOperator(
                                Objects.requireNonNull(Ip2Region.parseIp(ip)).getRegion()
                        );
                        NetInfoBean.setNetWorkOperator(area);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    /**
     * 获取小区信息，获取前先对小区信息进行重置
     */
    static void getCellInfo() {


        int lac = -1;
        int cellId = -1;
        int pci = -1;
        TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        List<CellInfo> infos = tm != null ? tm.getAllCellInfo() : null;
        if (infos != null && infos.size() > 0) {
            int i = 0;
            while (i < infos.size()) {
                CellInfo info = infos.get(i);
                i++;
                //没有注册直接跳过
                if (!info.isRegistered()) {
                    continue;
                }
                if (info instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) info;
                    CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                    Log.d(TAG,"cellIdentityLte------------------"+cellIdentityLte.toString());

                    //2147483647是一个无效值  直接跳过
                    if (cellIdentityLte.getTac() != 2147483647 &&
                            cellIdentityLte.getCi() != 2147483647) {
                        lac = cellIdentityLte.getTac();
                        cellId = cellIdentityLte.getCi();
                        pci = cellIdentityLte.getPci();
                        break;
                    }
                } else if (info instanceof CellInfoWcdma) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) info;
                    CellIdentityWcdma cellIdentityWcdma = null;
                    cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    //2147483647是一个无效值  直接跳过
                    if (cellIdentityWcdma.getLac() != 2147483647 &&
                            cellIdentityWcdma.getCid() != 2147483647) {
                        lac = cellIdentityWcdma.getLac();
                        cellId = cellIdentityWcdma.getCid();
                        break;
                    }
                } else if (info instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
                    CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                    //2147483647是一个无效值  直接跳过
                    if (cellIdentityCdma.getNetworkId() != 2147483647 &&
                            cellIdentityCdma.getBasestationId() != 2147483647) {
                        lac = cellIdentityCdma.getNetworkId();
                        cellId = cellIdentityCdma.getBasestationId();
                        break;
                    }
                } else if (info instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                    CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                    if (cellIdentityGsm.getLac() != 2147483647 &&
                            cellIdentityGsm.getCid() != 2147483647) {
                        lac = cellIdentityGsm.getLac();
                        cellId = cellIdentityGsm.getCid();
                        break;
                    }
                }
                if (i == infos.size()) {
                    if (lac == -1) {
                        lac = getLac2(tm);
                    }
                    if (cellId == -1) {
                        cellId = getCid2(tm);
                    }
                }
            }
        } else {
            lac = getLac2(tm);
            cellId = getCid2(tm);
        }
        if (NetInfoBean.simState == NetInfoBean.SIM_ONE) {
            if (lac != -1) {
                NetInfoBean.setTac(String.valueOf(lac));
            }
            if (cellId != -1) {
                NetInfoBean.setEci(cellId + "(" + cellId / 256 + "-" + cellId % 256 + ")");
            }
            if (pci != -1) {
                NetInfoBean.setPci(String.valueOf(pci));
            }
        } else if (NetInfoBean.simState == NetInfoBean.SIM_TWO) {
            if (lac != -1) {
                NetInfoBean.setTac2(String.valueOf(lac));
            }
            if (cellId != -1) {
                NetInfoBean.setEci2(cellId + "(" + cellId / 256 + "-" + cellId % 256 + ")");
            }
            if (pci != -1) {
                NetInfoBean.setPci2(String.valueOf(pci));
            }
        }
    }

    private static int getCid2(TelephonyManager tm) {
        int cellId = -1;
        @SuppressLint("MissingPermission") CellLocation cellLocation = tm != null ? tm.getCellLocation() : null;
        if (cellLocation == null) {
            return cellId;
        }
        Object o2 = ReflectUtils.reflect(cellLocation).method("getCid").get();
        if (o2 != null &&
                o2.toString().length() > 0 &&
                !o2.toString().equals("2147483647")) {
            cellId = Integer.parseInt(o2.toString());
        }
        return cellId;
    }

    private static int getLac2(TelephonyManager tm) {
        int lac = -1;
        @SuppressLint("MissingPermission")
        CellLocation cellLocation = tm != null ? tm.getCellLocation() : null;
        if (cellLocation == null) {
            return lac;
        }
        Object o1 = ReflectUtils.reflect(cellLocation).method("getLac").get();
        if (o1 != null &&
                o1.toString().length() > 0 &&
                !o1.toString().equals("2147483647")) {
            lac = Integer.parseInt(o1.toString());
        }
        return lac;
    }

    public static String getNetWorkIs234G() {
        String netWorkName = "未知网络类型";
        TelephonyManager mTelephonyManager = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkType 返回一个常数，表示目前在设备上使用的无线电技术（网络类型）
        if (mTelephonyManager != null) {
            switch (mTelephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS://1
                case TelephonyManager.NETWORK_TYPE_EDGE://2
                case TelephonyManager.NETWORK_TYPE_CDMA://4
                case TelephonyManager.NETWORK_TYPE_1xRTT://7
                case TelephonyManager.NETWORK_TYPE_IDEN: //11
                case TelephonyManager.NETWORK_TYPE_GSM:  //16
                    netWorkName = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS://3
                case TelephonyManager.NETWORK_TYPE_EVDO_0://5
                case TelephonyManager.NETWORK_TYPE_EVDO_A://6
                case TelephonyManager.NETWORK_TYPE_HSDPA://8
                case TelephonyManager.NETWORK_TYPE_HSUPA://9
                case TelephonyManager.NETWORK_TYPE_HSPA://10
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //12
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //14
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:  //17
                    netWorkName = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE://13
                case TelephonyManager.NETWORK_TYPE_IWLAN://18
                    netWorkName = "4G";
                    break;
                case 19:
                    netWorkName = "4G+";
                    break;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN://0
                    netWorkName = "未知网络类型";
                    break;
            }
        }
        return netWorkName;
    }

    private List<Long> upFlowList; //流量上传集合
    private List<Long> downFlowList; //流量下载集合
    private long currentUpFlow;//当前上传流量
    private long currentDownFlow;//当前下载流量
    private long maxUpFlow;//最大上传流量
    private long maxDownFlow;//最大下载流量
    private long avgUpFlow;//平均上传流量
    private long avgDownFlow;//平均下载流量
    private long lastUpFlow = 0;//上次上传流量
    private long thisUpFlow = 0;//当前上传流量
    private long lastDownFlow = 0;//上次下载流量
    private long thisDownFlow = 0;//当前下载流量
    private NetSpeedHandler netSpeedHandler;
    private NetSpeedCurrentListener netSpeedCurrentListener;

    public void startNetSpeed(NetSpeedCurrentListener netSpeedCurrentListener) {
        this.netSpeedCurrentListener = netSpeedCurrentListener;
        isRun = true;
        netSpeedHandler = new NetSpeedHandler();
        lastUpFlow = getTotalUpBytes();
        lastDownFlow = getTotalDownBytes();
        upFlowList = new ArrayList<>();
        downFlowList = new ArrayList<>();
        netSpeedHandler.postDelayed(netSpeedRunnable, 1000);
    }

    public void stopNetSpeed() {
        netSpeedHandler.removeCallbacks(netSpeedRunnable);
        resetNetSpeed();
    }

    public void stopNetSpeed(NetSpeedStopListener netSpeedStopListener) {
        netSpeedHandler.removeCallbacks(netSpeedRunnable);
        netSpeedStopListener.stop(maxDownFlow, avgDownFlow);
        resetNetSpeed();
    }

    private Runnable netSpeedRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("ysk-test", "记录网络速度---");
            thisUpFlow = getTotalUpBytes();
            thisDownFlow = getTotalDownBytes();
            upFlowList.add(thisUpFlow);
            downFlowList.add(thisDownFlow);
            NetInfoBean.setNetDownSpeed(String.valueOf(thisDownFlow - lastDownFlow));
            NetInfoBean.setNetUpSpeed(String.valueOf(thisUpFlow - lastUpFlow));
            netSpeedCurrentListener.current(thisUpFlow - lastUpFlow, thisDownFlow - lastDownFlow);
            if (isRun) {
                netSpeedHandler.postDelayed(netSpeedRunnable, 1000);
            }
            lastUpFlow = thisUpFlow;
            lastDownFlow = thisDownFlow;
        }
    };

    private static class NetSpeedHandler extends Handler {
    }

    /**
     * 重置网速监听所有参数
     */
    private void resetNetSpeed() {
        currentUpFlow = 0;
        currentDownFlow = 0;
        maxUpFlow = 0;
        maxDownFlow = 0;
        avgUpFlow = 0;
        avgDownFlow = 0;
        lastUpFlow = 0;
        thisUpFlow = 0;
        lastDownFlow = 0;
        thisDownFlow = 0;
        upFlowList.clear();
        downFlowList.clear();
        isRun = false;
    }

    private static long getTotalDownBytes() {  //总的接受字节数，包含Mobile和WiFi等
        return TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

    private static long getTotalUpBytes() {  //总的发送字节数，包含Mobile和WiFi等
        return TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalTxBytes() / 1024);
    }

    public interface NetSpeedCurrentListener {
        void current(long upSpeed, long downSpeed);
    }

    public interface NetSpeedStopListener {
        void stop(long maxDownSpeed, long avgDownSpeed);

        void stop(long maxUpSpeed, long maxDownSpeed, long avgUpSpeed, long avgDownSpeed);
    }
}
