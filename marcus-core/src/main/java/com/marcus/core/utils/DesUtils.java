package com.marcus.core.utils;

import com.marcus.base.exception.BusinessException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;

public abstract class DesUtils {

    private static final String DES             =       "DES";

    private static final String TRANSFORMATION  =       "DES/ECB/NoPadding";

    public static final String DEFAULT_KEY = "609610zzl12345678998765432156789";




    /**
     * 根据转换方式进行DES加密(仅支持单倍长密钥)
     *
     * @param data           需要加密的数据
     * @param key            密钥明文
     * @param transformation 转换方式字符串
     */
    private static byte[] getEncrypt(byte[] data, byte[] key, String transformation) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new BusinessException(-1,"DES加密失败");
        }
    }

    private static byte[] getEncrypt(byte[] data, byte[] key) {
        return getEncrypt(data, key, TRANSFORMATION);
    }

    /**
     * 根据转换方式进行DES解密(仅支持单倍长密钥)
     *
     * @param key            密钥明文
     * @param transformation 转换方式字符串
     */
    private static byte[] getDecrypt(byte[] byteS, byte[] key, String transformation) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(byteS);
        } catch (Exception e) {
            throw new RuntimeException("DES解密失败");
        }
    }

    private static byte[] getDecrypt(byte[] byteS, byte[] key) {
        return getDecrypt(byteS, key, "DES/ECB/NoPadding");
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static byte[] hexStringToBytes(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i<<1;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * DES加密(支持单倍长、双倍长、三倍长密钥)
     *
     * @param data 需要加密的数据
     * @param key  密钥明文
     */
    private static byte[] encrypt(byte[] data, String key) {

        if (key.length() == 48) {
            byte[] bytK1 = hexStringToBytes(key.substring(0, 16));
            byte[] bytK2 = hexStringToBytes(key.substring(16, 32));
            byte[] bytK3 = hexStringToBytes(key.substring(32, 48));
            return getEncrypt(getDecrypt(getEncrypt(data, bytK1), bytK2), bytK3);
        } else if (key.length() == 32) {
            byte[] bytK1 = hexStringToBytes(key.substring(0, 16));
            byte[] bytK2 = hexStringToBytes(key.substring(16, 32));
            return getEncrypt(getDecrypt(getEncrypt(data, bytK1), bytK2), bytK1);
        } else if (key.length() == 16) {
            return getEncrypt(data, hexStringToBytes(key));
        } else {
            throw new BusinessException(-1,"DES加密失败，密文长度有误[len = " + key.length() + "]");
        }
    }

    /**
     * DES解密(支持单倍长、双倍长、三倍长密钥)
     *
     * @param data 需要加密的数据
     * @param key  密钥明文
     */
    private static byte[] decrypt(byte[] data, String key) {

        if (key.length() == 48) {
            byte[] bytK1 = hexStringToBytes(key.substring(0, 16));
            byte[] bytK2 = hexStringToBytes(key.substring(16, 32));
            byte[] bytK3 = hexStringToBytes(key.substring(32, 48));
            return getDecrypt(getEncrypt(getDecrypt(data, bytK3), bytK2), bytK1);
        } else if (key.length() == 32) {
            byte[] bytK1 = hexStringToBytes(key.substring(0, 16));
            byte[] bytK2 = hexStringToBytes(key.substring(16, 32));
            return getDecrypt(getEncrypt(getDecrypt(data, bytK1), bytK2), bytK1);
        } else if (key.length() == 16) {
            return getDecrypt(data, hexStringToBytes(key));
        } else {
            throw new BusinessException(-1,"DES解密失败，密文长度有误[len = " + key.length() + "]");
        }
    }

    private static byte[] afterFillingWithSpace(byte[] orgi, int lastLength) {

        if (orgi == null){
            return null;
        }
        if (orgi.length == lastLength){
            return orgi;
        }
        byte[] data = new byte[lastLength];
        int len = orgi.length > lastLength ? lastLength:orgi.length;
        System.arraycopy(orgi, 0, data, 0, len);
        for (int i = len; i < lastLength; i++){
            data[i] = '\0';
        }
        return data;
    }

    private static byte[] encryptFillingWithSpace(byte[] data, String key) {

        int len = data.length;
        int bs = len;
        if (len % 8 != 0) {
            bs = (len / 8 + 1) * 8;
        }
        return encrypt(afterFillingWithSpace(data, bs), key);
    }

    private static String decryptRemoveSpace(byte[] data, String key, String encoding) {
        return new String(decrypt(data, key), Charset.forName(encoding)).trim();
    }

    public static String decodeAndDecrypt(String data, String key, String encoding) {
        byte[] decryptStr = Base64.decodeBase64(data);
        return decryptRemoveSpace(decryptStr, key,encoding);
    }


    public static String encryptAndEncode(byte[] date, String key) {
        return Base64.encodeBase64String(encryptFillingWithSpace(date, key));
    }

    public static String encryptAndEncode(String data, String key,Charset charset) {
        return Base64.encodeBase64String(encryptFillingWithSpace(data.getBytes(charset), key));
    }

    public static String encryptAndEncode(String data, String key, String encoding) {
        return Base64.encodeBase64String(encryptFillingWithSpace(data.getBytes(Charset.forName(encoding)), key));
    }
}
