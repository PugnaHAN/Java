package com.hp.printsdk;

/**
 * Created by zhangjuh on 2016/6/29.
 */
public interface IPrintTaskManager {
    /**
     * Initialize the print tasks
     */
    void init();

    /**
     * Clear all the print tasks
     */
    void reset();

    /**
     * Update the print tasks to the corresponding queue
     */
    void refresh();

    /**
     * Start print the highest priority task
     */
    void start();

    /**
     * Start print the task which you are interested in
     * @param task  - the task which you wanna print
     */
    void start(PrintTask task);

    /**
     * add the print task into the toprint quque
     * @param task
     */
    void add(PrintTask task);

    /**
     * Remove the task from waiting list
     * @param task
     */
    void remove(PrintTask task);
}
