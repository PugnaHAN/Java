package com.hp.printsdk;

/**
 * Created by zhangjuh on 2016/6/29.
 */
public interface IPrintTaskManager {
    void init();
    void reset();
    void refresh();
    void start();
    void add(PrintTask task);
    void remove(PrintTask task);
}
