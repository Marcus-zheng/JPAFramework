package com.marcus.core.utils;

import com.sun.crypto.provider.SunJCE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @ClassName EncrypAESUtil
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/25 15:47
 * @Version 1.0
 **/
public class EncrypAESUtil {
    private static Logger logger = LoggerFactory.getLogger(EncrypAESUtil.class);
    private static KeyGenerator keygen;
    private static SecretKey deskey;
    private static Cipher cipher;
    private static Cipher cipherDe;

    public EncrypAESUtil() {
    }

    public static byte[] encrytor(String str) {
        try {
            byte[] src = str.getBytes("utf-8");
            byte[] cipherByte = cipher.doFinal(src);
            return cipherByte;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String encrytToString(String str) {
        byte[] bytes = encrytor(str);
        return parseByte2HexStr(bytes);
    }

    public static byte[] decryptor(byte[] buff) {
        try {
            byte[] cipherByte = cipherDe.doFinal(buff);
            return cipherByte;
        } catch (Exception var9) {
            try {
                logger.debug("+++" + buff.length + "+++" + (new String(buff, "utf-8")).toString());
            } catch (UnsupportedEncodingException var7) {
                ;
            } finally {
                throw new RuntimeException(var9);
            }
        }
    }

    public static String decryptToString(String str) {
        try {
            byte[] bytes = parseHexStr2Byte(str);
            return new String(decryptor(bytes), "utf-8");
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for(int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte)(high * 16 + low);
            }

            return result;
        }
    }

    static {
        Security.addProvider(new SunJCE());

        try {
            keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            String key = "cipher";
            secureRandom.setSeed(key.getBytes());
            keygen.init(128, secureRandom);
            deskey = keygen.generateKey();
            cipher = Cipher.getInstance("AES");
            cipher.init(1, deskey);
            cipherDe = Cipher.getInstance("AES");
            cipherDe.init(2, deskey);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

//    public static void main(String[] args) throws Exception
//    {
//        String msg = "Company Name";
//        logger.debug("明文是:" + msg);
//        String s = EncrypAESUtil.encrytToString(msg);
//        logger.debug("加密后:" + s);
//        System.out.println("解密后:" + EncrypAESUtil.decryptToString("A85B321BDCACC5599EB7D80EE7779C94"));
//    }
}
