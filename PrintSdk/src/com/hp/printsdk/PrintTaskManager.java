package com.hp.printsdk;

import org.omg.SendingContext.RunTime;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by zhangjuh on 2016/6/29.
 */
public class PrintTaskManager implements IPrintTaskManager{
    private static final String TAG = PrintTaskManager.class.getSimpleName();

    private static final int MAX_FREE_QUEUE_LENGTH = 10;

    // SingleTon because only one manager can exist
    private static PrintTaskManager sPrintTaskManager = null;

    private ArrayList<PrintTask> mToPrintQueue = new ArrayList<>();
    private ArrayList<PrintTask> mDoneTasks = new ArrayList<>();
    private ArrayList<PrintTask> mAbortedTasks = new ArrayList<>();

    // A variable is used to forbid two or more thread to handle it
    private Thread mMaster = null;

    // Constructor
    private PrintTaskManager(ArrayList<PrintTask> tasks) {
        mToPrintQueue = tasks;
    }

    // Get the single instance of Manager
    // Double check method to avoid multiple threads issue
    public static PrintTaskManager getInstance() {
        if(sPrintTaskManager == null) {
            synchronized (PrintTaskManager.class) {
                if(sPrintTaskManager == null) {
                    sPrintTaskManager = new PrintTaskManager(new ArrayList<>());
                }
            }
        }
        return sPrintTaskManager;
    }

    /* Get an set methods */
    public ArrayList<PrintTask> getToPrintQueue() {
        return mToPrintQueue;
    }

    public void setToPrintQueue(ArrayList<PrintTask> toPrintQueue) {
        mToPrintQueue = toPrintQueue;
    }

    public ArrayList<PrintTask> getDoneTasks() {
        return mDoneTasks;
    }

    public ArrayList<PrintTask> getAbortedTasks() {
        return mAbortedTasks;
    }

    public Thread getMaster() {
        return mMaster;
    }

    public void setMaster(Thread owner) {
        mMaster = owner;
    }

    /**
     * Check whether the manager has owner or not
     * @return false -> no master; true -> has master
     */
    public boolean hasMaster() {
        return mMaster != null;
    }

    /**
     * add the task into the list with order
     * @param task - the task which should be added
     */
    @Override
    public void add(PrintTask task) {
        if(!hasPermission()) {
            return;
        }

        int len = mToPrintQueue.size();
        if(len == 0) {
            mToPrintQueue.add(task);
        } else if(len == 1) {
            mToPrintQueue.add((task.compareTo(mToPrintQueue.get(0)) > 0? 1 : 0),
                    task);
        } else {
            int index;
            for(index = 0; index < len -1; index++) {
                if(task.compareTo(mToPrintQueue.get(index)) >=0 &&
                        task.compareTo(mToPrintQueue.get(index + 1)) < 0) {
                    break;
                }
                if(task.compareTo(mToPrintQueue.get(0)) < 0) {
                    index = 0;
                    break;
                }
            }
            mToPrintQueue.add(index + 1, task);
        }
    }

    /**
     * remove the task from mToPrintQueue
     * @param task - the task which should be removed
     */
    @Override
    public void remove(PrintTask task) {
        if(!hasPermission()) {
            return;
        }

        mToPrintQueue.remove(task);
    }

    /**
     * Initialize the three pools
     */
    @Override
    public void init() {
        if(!hasPermission()) {
            return;
        }

        arrangeWaitingList();
        arrangeAbortedList();
        arrangeDoneTasks();
    }

    /**
     * reset all the pools to empty
     */
    @Override
    public void reset() {
        if(!hasPermission()) {
            return;
        }

        mAbortedTasks.clear();
        mDoneTasks.clear();
        mDoneTasks.clear();
    }

    /**
     * Add all the tasks to correct list
     */
    @Override
    public void refresh() {
        if(!hasPermission()) {
            return;
        }

        // Remove the invalid tasks from toprintqueue to abortqueue or donequeue
        for(PrintTask task : mToPrintQueue) {
            updateTask(task);
        }
    }

    /**
     * Start the top valid task in the mToPrintQueue
     */
    @Override
    public synchronized void start() {
        if(!hasPermission()) {
            return;
        }

        if(mToPrintQueue.size() > 0) {
            PrintTask task = mToPrintQueue.get(0);
            task.setOnUpdateListener(() -> {
                updateTask(task);
            });
            task.start();
        }
    }

    // Update single task to correct list
    private void updateTask(PrintTask task) {
        switch (task.getCurrentStatus()) {
            case ABORTED:
                mToPrintQueue.remove(task);
                mAbortedTasks.add(task);
                break;
            case SUCCESSFUL:
            case FAILED:
                mToPrintQueue.remove(task);
                arrangeDoneTasks();
                mDoneTasks.add(task);
                break;
            default:
                break;
        }
    }

    // Arrange the three Lists to make them easy to manage
    private void arrangeDoneTasks() {
        int len = mDoneTasks.size();
        if(len >= MAX_FREE_QUEUE_LENGTH) {
            for(int i = len -1; i >= MAX_FREE_QUEUE_LENGTH - 1; i--) {
                mDoneTasks.remove(i);
            }
        }
    }

    private void arrangeWaitingList() {
        if(mToPrintQueue.size() > 1) {
            Collections.sort(mToPrintQueue);
        }
    }

    private void arrangeAbortedList() {
        if(mAbortedTasks.size() > 1) {
            Collections.sort(mAbortedTasks);
        }
    }

    private boolean hasPermission() {
        if(getMaster() == null) {
            return true;
        } else {
            return Thread.currentThread().equals(getMaster()) ;
        }
    }
}
