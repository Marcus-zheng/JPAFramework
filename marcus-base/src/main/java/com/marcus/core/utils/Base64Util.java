package com.marcus.core.utils;

import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 14:59
 **/
public class Base64Util
{
    /**字符串加密*/
    public static String getBase64(String str) {
        String encodedText = null;
        byte[] textByte = null;

        try {
            if(str != null) {
                textByte = str.getBytes("UTF-8");
                encodedText = encode(textByte);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedText;
    }

    /**字符串解密*/
    public static String getFromBase64(String str) {
        String encodedText = null;
        byte[] textByte = null;

        try {
            if(str != null) {
                textByte = decode(str);
                encodedText = new String(textByte, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedText;
    }

    /**字节数组加密*/
    public static String encode(byte[] data) {
        String encodedText = null;
        if(data != null) {
            encodedText = Base64.getEncoder().encodeToString(data);
        }
        return encodedText;
    }

    /**字节数组解码*/
    public static byte[] decode(String str) {
        byte[] textByte = null;
        if(str != null) {
            textByte = Base64.getDecoder().decode(str);
        }
        return textByte;
    }
    /**
     * @Description:  对字节数组字符串进行Base64解码并生成图片
     * @Author Marcus.zheng
     * @Date 2019/8/19 15:01
     * @Param imgStr  Base64码
     * @Param imgFilePath  图片文件保存地址
     * @Return boolean
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        // 图像数据为空
        if(imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    // 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @Description: (将一张网络图片转化成Base64字符串)
     * @param imgURL 网络资源位置
     * @return Base64字符串
     */
    public static String getImageStrFromUrl(String imgURL) {
        byte[] data = getImageFromNetByUrl(imgURL);
        String content = Base64Utils.encodeToString(data);
        return content;
    }

    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

//	public static void main(String[] args)
//	{
//		String aa = "abc";
//		String en = Base64Util.getBase64(aa);
//		System.out.println("加密==="+en);
//		aa = Base64Util.getFromBase64(en);
//		System.out.println("解码==="+aa);
//        System.out.println(getImageStrFromUrl("http://dfs.aixahc.com:7000/group1/M00/00/16/wKhkXVt76W6AElyMAAAUl8vSZq0142.jpg?u=0f28562f44bd4ae3bd0f92a152ba74d5"));
//
//	}

}
