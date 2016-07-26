package com.hp.printsdk.printer;

/**
 * Created by zhangjuh on 2016/7/13.
 */
public class Log {
    private static void log(String tag, String log) {
        System.out.println(tag + ": " + log);
    }

    public static void d(String tag, String log) {
        log(tag, log);
    }

    public static void e(String tag, String log) {
        log(tag, log);
    }
}
