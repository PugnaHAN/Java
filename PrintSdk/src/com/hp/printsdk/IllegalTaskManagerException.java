package com.hp.printsdk;

/**
 * Created by zhangjuh on 2016/6/30.
 */
public class IllegalTaskManagerException extends Exception {
    private static final String TAG = IllegalTaskManagerException.class.getSimpleName();

    public IllegalTaskManagerException() {
        super();
    }

    public IllegalTaskManagerException(String msg) {
        super(msg);
    }
}
