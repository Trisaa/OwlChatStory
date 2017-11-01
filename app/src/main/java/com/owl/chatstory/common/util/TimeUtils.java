package com.owl.chatstory.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lebron on 2017/11/1.
 */

public class TimeUtils {
    public static String getTimeFormat(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        String t = format.format(new Date(time));
        return t;
    }
}
