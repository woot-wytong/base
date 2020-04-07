package com.hairun.net_info;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;

import org.lionsoul.ip2region.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * IP地址查询工具
 */
public class Ip2Region {
    private static DbSearcher searcher = null;
    private static Logger logger = Logger.getLogger(Ip2Region.class.getName());
    private static final String DB_NAME = "ip2region.db";
    private static final String DB_PATH = CommonPath.getDocumentPath() + "/opt";

    static {
        String dbfile = DB_PATH + "/" + DB_NAME;
        if(!new File(dbfile).exists()){
            ResourceUtils.copyFileFromAssets(
                    "ip2region.db",
                    CommonPath.getDocumentPath()+"/opt/ip2region.db"
            );
        }

        try {
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, dbfile);
        } catch (DbMakerConfigException e) {
            logger.warning("ip2region config init exception:" + e.getMessage());
        } catch (FileNotFoundException e) {
            logger.warning("ip2region file not found" + e.getMessage());
        }
    }

    public static DataBlock parseIp(String ip) {
        boolean isIpAddress = isIp(ip);
        if (isIpAddress) {
            try {
                return searcher.btreeSearch(ip);
            } catch (IOException e) {
                logger.warning("ip2region parse error" + e.getMessage());
            }
        }
        return null;
    }

    private static boolean isIp(String IP) {//判断是否是一个IP
        boolean b = false;
        if (IP == null) {
            return b;
        } else {
            IP = trimSpaces(IP);
            if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                String[] s = IP.split("\\.");
                if (Integer.parseInt(s[0]) < 255)
                    if (Integer.parseInt(s[1]) < 255)
                        if (Integer.parseInt(s[2]) < 255)
                            if (Integer.parseInt(s[3]) < 255)
                                b = true;
            }
            return b;
        }
    }

    private static String trimSpaces(String IP) {//去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;

    }

    /**
     * 截取归属地 “中国陕西省移动”
     */
    public static String cult(String s) {
        String str1 = s.split("\\|")[0].equals("0") ? "" : s.split("\\|")[0];
        String str2 = s.split("\\|")[2].equals("0") ? "" : s.split("\\|")[2];
        String str3 = s.split("\\|")[3].equals("0") ? "" : s.split("\\|")[3];
        String str4 = s.split("\\|")[4].equals("0") ? "" : s.split("\\|")[4];
        return str1 + str2 + str3 + str4;
    }

    /**
     * 截取归属地 “中国陕西省”
     */
    public static String cultAddress(String s) {
        String str1 = s.split("\\|")[0].equals("0") ? "" : s.split("\\|")[0];
        String str2 = s.split("\\|")[2].equals("0") ? "" : s.split("\\|")[2];
        String str3 = s.split("\\|")[3].equals("0") ? "" : s.split("\\|")[3];
        return str1 + str2 + str3;
    }

    /**
     * 截取运营商
     *
     * @return "移动"
     */
    public static String cultOperator(String s) {
        return s.split("\\|")[4].equals("0") ? "" : s.split("\\|")[4];
    }

}

