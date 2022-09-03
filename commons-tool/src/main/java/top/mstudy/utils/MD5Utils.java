package top.mstudy.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author machao
 * @description: MD5加密工具类
 * @date 2022-09-02
 */
@Slf4j public class MD5Utils {

    private static MessageDigest md5 = null;

    private static MessageDigest getMD5() {
        if (md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException e) {
                log.error("in Md5Util:", e);
            }
        }
        return md5;
    }

    /**
     * MD5加密小写32位
     *
     * @param src
     * @param encoding 字符集
     * @return
     */
    public static final String md5LowerCase(String src, String encoding) {
        try {
            MessageDigest md = getMD5();
            md.update(src.getBytes(encoding));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }
            return buf.toString();// 32位的加密
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * MD5加密 大写,16位
     *
     * @param src
     * @param encoding
     * @return
     */
    public static final String md5UpperCase(String src, String encoding) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = src.getBytes(encoding);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = getMD5();
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            // byte[] data = { (byte) 0xfe, (byte) 0xff, 0x00, 0x61 };
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).substring(8, 24);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * MD5加密 大写,32位
     *
     * @param src
     * @param encoding
     * @return
     */
    public static final String md5UpperCase32(String src, String encoding) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = src.getBytes(encoding);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = getMD5();
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            // byte[] data = { (byte) 0xfe, (byte) 0xff, 0x00, 0x61 };
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 对一个文件获取md5值
     *
     * @return md5串
     */
    public static String getMd5File(File file) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            md5 = getMD5();
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(encodeHex(md5.digest()));
        }
        catch (Exception e) {
            log.error("in Md5Util:", e);
            return null;
        }
    }

    /**
     * 对一个文件获取md5值
     *
     * @return md5串
     */
    public static String getMd5File(String filePath) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            md5 = getMD5();
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(encodeHex(md5.digest()));
        }
        catch (Exception e) {
            log.error("in Md5Util:", e);
            return null;
        }
    }

    private static final char[] DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f' };

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int var4 = 0; i < l; ++i) {
            out[var4++] = DIGITS[(240 & data[i]) >>> 4];
            out[var4++] = DIGITS[15 & data[i]];
        }

        return out;
    }

    /**
     * MD5值校验是否一致
     *
     * @param inputStr
     * @param md5Str
     * @return
     * @throws Exception
     */
    public static Boolean compareStrMd5(String inputStr, String md5Str) throws Exception {
        //获取输入字符串的MD5值
        String md5StrNew = getMd5File(inputStr);
        if (md5StrNew.equals(md5Str)) {
            log.info("校验成功！");
            return true;
        }
        log.info("校验失败！");
        return false;
    }
}

