package com.example.accesstest.util;

import android.util.Log;

/**
 *  Created by Simon Chang on 2018/01/25
 *  使用Log來顯示調試信息,因為log在實現上每個message有4k字符長度限制
 *  所以這裡使用自己分節的方式來輸出足夠長度的message
 */

public class L {

    public static void v(String tag, String str) {
        int index = 0;
        int buffer = 3000;
        String sub;
        while (str.length() > index + buffer) {
            sub = str.substring(index, index + buffer);
            index += buffer;

            Log.v(tag, sub);
        }

        sub = str.substring(index);
        Log.v(tag, sub);
    }

    public static void d(String tag, String str) {
        int index = 0;
        int buffer = 3000;
        String sub;
        while (str.length() > index + buffer) {
            sub = str.substring(index, index + buffer);
            index += buffer;

            Log.d(tag, sub);
        }

        sub = str.substring(index);
        Log.d(tag, sub);
    }

    public static void i(String tag, String str) {
        int index = 0;
        int buffer = 3000;
        String sub;
        while (str.length() > index + buffer) {
            sub = str.substring(index, index + buffer);
            index += buffer;

            Log.i(tag, sub);
        }

        sub = str.substring(index);
        Log.i(tag, sub);
    }

    public static void w(String tag, String str) {
        int index = 0;
        int buffer = 3000;
        String sub;
        while (str.length() > index + buffer) {
            sub = str.substring(index, index + buffer);
            index += buffer;

            Log.w(tag, sub);
        }

        sub = str.substring(index);
        Log.w(tag, sub);
    }

    public static void e(String tag, String str) {
        int index = 0;
        int buffer = 3000;
        String sub;
        while (str.length() > index + buffer) {
            sub = str.substring(index, index + buffer);
            index += buffer;

            Log.e(tag, sub);
        }

        sub = str.substring(index);
        Log.e(tag, sub);
    }
}