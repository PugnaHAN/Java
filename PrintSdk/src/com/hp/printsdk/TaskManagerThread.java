package com.hp.printsdk;

/**
 * Created by zhangjuh on 2016/6/30.
 */
public class TaskManagerThread extends Thread {
    private static final String TAG = TaskManagerThread.class.getSimpleName();

    private PrintTaskManager mTaskManager;

    public TaskManagerThread() throws IllegalTaskManagerException {
        super();
        mTaskManager = PrintTaskManager.getInstance();
        if(!mTaskManager.hasMaster()) {
            throw new IllegalTaskManagerException("One TaskManagerThread has existed!");
        } else {
            mTaskManager.setMaster(this);
        }
    }

    public TaskManagerThread(Runnable run) throws IllegalTaskManagerException {
        super(run);
        mTaskManager = PrintTaskManager.getInstance();
        if(mTaskManager.hasMaster()) {
            throw new IllegalTaskManagerException("One TaskManagerThread has existed!");
        } else {
            mTaskManager.setMaster(this);
        }
    }
}
