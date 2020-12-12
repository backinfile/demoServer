package com.backinfile.utils;

public class Time2 {
    public static final long MILLI  = 1L;
    public static final long SECOND = 1000L * MILLI;
    public static final long MINUTE = 60L * SECOND;
    public static final long HOUR   = 60L * MINUTE;
    public static final long Day    = 24L * HOUR;


    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}
