package com.marcus.startup.utils;

import com.marcus.core.utils.DesUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 设置Cookie
 * @author Charles
 * @since 2019-07-12
 */
public abstract class CookieHelper {


    public static void setCookie(HttpServletResponse response, String key, String value, int maxAge){


        Cookie cookie = new Cookie(key, DesUtils.encryptAndEncode(value,DesUtils.DEFAULT_KEY, StandardCharsets.UTF_8));
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

    }


    private static Cookie getCookie(HttpServletRequest request, String key){

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0)
        {
            for (Cookie cookie : cookies)
            {
                String name = cookie.getName();

                if (key.equals(name))
                {
                    return cookie;
                }
            }
        }

        return null;

    }

    public static String getCookieValue(HttpServletRequest request, String key){

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0)
        {

            for (Cookie cookie : cookies)
            {
                String name = cookie.getName();

                if (key.equals(name))
                {
                    String value = cookie.getValue();
                    return DesUtils.decodeAndDecrypt(value, DesUtils.DEFAULT_KEY, "utf-8");
                }
            }
        }

        return null;

    }


    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String key){

        Cookie cookie = getCookie(request, key);

        if (cookie != null)
        {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }

}
