package com.tap.chatstory.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lebron on 2017/11/1.
 */

public class TimeUtils {
    public static String getTimeFormat(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        String t = format.format(new Date(time * 1000L));
        return t;
    }

    public static String getNumFormat(int num) {
        if (num >= 1000) {
            int hundred = num / 100;
            float result = (float) hundred / 10.0f;
            return result + "k";
        } else {
            return String.valueOf(num);
        }
    }
}
