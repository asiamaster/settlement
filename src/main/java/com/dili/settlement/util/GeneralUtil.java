package com.dili.settlement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 工具类
 */
public class GeneralUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralUtil.class);

    /**
     * 生成32位uuid
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取md5加密字符串
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return parseByte2Hex(messageDigest.digest(str.getBytes("UTF-8")));
        } catch (Exception e) {
            LOGGER.error("method md5", e);
        }
        return "";
    }

    private static String parseByte2Hex(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
