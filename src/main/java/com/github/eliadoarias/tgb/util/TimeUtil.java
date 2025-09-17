package com.github.eliadoarias.tgb.util;

import java.util.Date;

public class TimeUtil {
    public static long getCurrentTime(){
        return System.currentTimeMillis();
    };
    public static Date getDateNow() {
        return new Date();
    }
    public static Date getDateAfter(long time) {
        return new Date(getCurrentTime() + time);
    }
}
