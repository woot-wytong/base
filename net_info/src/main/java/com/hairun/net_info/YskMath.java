package com.hairun.net_info;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2017/4/1.
 */

public class YskMath {

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被減数
     * @param v2 減数
     * @return两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除法运算
     *
     * @param m1
     * @param m2
     * @param scale
     * @return
     */
    public static double div(double m1, double m2, int scale) {
        if (m2 == 0 || Double.isNaN(m2)) {
            return 0;
        }
        if (scale < 0) {
            throw new IllegalArgumentException("Parameter error");
        }
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static String mathDivide(double s1, double s2) {
        if (s2 != 0) {
            double value = div(s1, s2, 2);
            return doubleToString_2(value);
        } else {
            return "0.00";
        }
    }

    public static String mathGetLengthToMB(double s1) {
        double value = div(div(s1, 1024, 2), 1024, 2);
        return doubleToString_2(value);
    }

    //计算百分比
    public static String mathRatio(double s1, double s2) {
        if (s2 != 0) {
            double value = mul(div(s1, s2, 2), 100);
//           Log.e("ysk-test---百分比转化", "s1:" + s1 + "---s2:" + s2 + "---value:" + value);
            return doubleToString_2(value);
        } else {
            return "0.00";
        }
    }

    //kqi速率计算
    public static String mathDivideRate(double s1, double s2) {
        s2 = div(s2, 1000, 2);
        if (s2 != 0) {
            double value = mul(div(s1, s2, 2), 8);
            return doubleToString_2(value);
        } else {
            return "0.00";
        }
    }

    public static String doubleToString_2(double d) {
        if (Double.isNaN(d)) {
            return "0.00";
        }
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String stringToString_2(String s) {
        if (s == null || s.equals("")) {
            return "0.00";
        }
        double d = Double.parseDouble(s);
        if (Double.isNaN(d)) {
            return "0.00";
        }
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static double stringToDouble_2(String s) {
        if (s == null || s.equals("")) {
            return 0.00;
        }
        double d = Double.parseDouble(s);
        if (Double.isNaN(d)) {
            return 0.00;
        }
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static double doubleToDouble_2(double d) {
        if (Double.isNaN(d)) {
            return 0.00;
        }
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 计算涨幅
     * 涨幅=(现价-上一个交易日收盘价）/上一个交易日收盘价*100%
     */
    public static String mathRisesToString_2(String history, String now) {
        if ((history == null || history.equals("")) || (now == null || now.equals(""))) {
            return "0.00";
        }

        double a = Double.valueOf(history);
        double b = Double.valueOf(now);
//        Log.e("ysk-test", "math：a-->" + a + "---" + "b-->" + b);
        if (a != 0) {
            double value = mul(div((b - a), a, 2), 100);
            value = Math.abs(value);
//            Log.e("ysk-test", "rises：" + value);
            return doubleToString_2(value);
        } else {
            return "0.00";
        }
    }

    public static double toDouble_2(double d) {
        if (Double.isNaN(d)) {
            return 0.00;
        }
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }


    public static String getPercent(double x, double y) {
        String result;
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        result = numberFormat.format(x / y * 100);
        return result;
    }

    public static String mathDivideSuccess(double s1, double s2) {
        if (s2 != 0) {
            double value = mul(div(s1, s2, 2), 100);
            return doubleToString_2(value);
        } else {
            return "0.00";
        }
    }
}
