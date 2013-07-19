package com.golfeven.firstGolf.common;

import android.util.Log;

public class MyLog {
    public static final int INFO = Log.INFO;
    public static boolean showLogD = true ;
    public static boolean showLogE = true ;
    public static boolean showLogI = true ;
    public static boolean showLogW = true ;
    public static boolean showLogV = true ;
    public static boolean showLogException = true ;
    public static boolean showLog = true ;

    public static void i(String tag, String msg) {
        if (showLog && showLogI) {
            Log.i(tag, msg); 
        }
    }
    public static void v(String tag, String msg) {
    	if (showLog && showLogV) {
    		Log.v(tag, msg); 
    	}
    }

    public static void e(String tag, String msg) {
        if (showLog && showLogE) {
            Log.e(tag, msg); 
         }
    }

    public static void w(String tag, String msg) {
        if (showLog && showLogW) {
            Log.w(tag, msg); 
         }
    }

    public static void d(String tag, String msg) {
        if (showLog && showLogD) {
            Log.d(tag, msg); 
         }
    }

    public static void exception(Exception e) {
        if (showLog && showLogException) {
           String msg = Log.getStackTraceString(e);
           Log.e("error",msg);
        }
    }

    public static void exception(OutOfMemoryError e) {
        if (showLog && showLogException) {
            e.printStackTrace();
        }
    }

    public static void exception(Throwable e) {
        if (showLog && showLogException) {
            e.printStackTrace();
        }
    }

    public static boolean isLoggable(String tag, int info2) {
        return Log.isLoggable(tag, info2);
    }

}
