package com.hp.printsdk;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class is used to record the information of single print task
 * @author Juhan Zhang - ju-han.zhang@hp.com
 */
public class PrintTask implements Comparable<PrintTask> {
    private static final String TAG = PrintTask.class.getSimpleName();

    private static final int DEFAULT_PRIORITY = 1;
    private static final int DEFAULT_DELAY_TIME = 0;

    // Preset timeout constance
    private static final long FIVE_MINUTES = 5 * 60 * 1000;
    private static final long TEN_MINUTES = 10 * 60 * 1000;
    private static final long HALF_HOUR = 30 * 60 * 1000;
    private static final long INFINITE = -1;
    /**
     * priority -> 0 should be the most urgent, maximum is 10
     */
    private int mPriority;
    private File mPrintFile;
    private long mSubmitTime;

    // Task status
    private TaskStatus mCurrentStatus;
    private TaskStatus mOldStatus;

    //  Status update listner
    private OnUpdateListener mOnUpdateListener;

    // It is generated by YEAR-MONTH-DAY-HOUR-MINUTE-SECOND depends on submit time
    private long mTaskId;

    // It is an optional choice
    private long mDelayTime;
    private long mTimeOut = INFINITE;

    /* Constructors */
    public PrintTask(int priority, File file, long delayTime) {
        mPriority = priority;
        mPrintFile = file;

        mDelayTime = delayTime;
        mSubmitTime = System.currentTimeMillis() + mDelayTime;
        mCurrentStatus = TaskStatus.TOPRINT;
    }

    public PrintTask(int priority, String filePath, long delayTime)
            throws FileNotFoundException {
        mPriority = priority;
        mPrintFile = new File(filePath);
        if (!mPrintFile.exists()) {
            throw new FileNotFoundException("Not a invalid path!");
        }

        mDelayTime = delayTime;
        mSubmitTime = System.currentTimeMillis() + mDelayTime;
        mCurrentStatus = TaskStatus.TOPRINT;
    }

    public PrintTask(int priority, File file) {
        mPriority = priority;
        mPrintFile = file;

        mDelayTime = DEFAULT_DELAY_TIME;
        mSubmitTime = System.currentTimeMillis() + mDelayTime;
        mCurrentStatus = TaskStatus.TOPRINT;
    }

    public PrintTask(int priority, String filePath)
            throws FileNotFoundException {
        mPriority = priority;
        mPrintFile = new File(filePath);
        if (!mPrintFile.exists()) {
            throw new FileNotFoundException("Not a invalid path!");
        }

        mDelayTime = DEFAULT_DELAY_TIME;
        mSubmitTime = System.currentTimeMillis() + mDelayTime;
        mCurrentStatus = TaskStatus.TOPRINT;
    }

    public PrintTask(File file) {
        mPriority = DEFAULT_PRIORITY;
        mPrintFile = file;

        mDelayTime = DEFAULT_DELAY_TIME;
        mSubmitTime = System.currentTimeMillis() + mDelayTime;
        mCurrentStatus = TaskStatus.TOPRINT;
    }

    public PrintTask(String filePath) throws FileNotFoundException {
        mPriority = DEFAULT_PRIORITY;
        mPrintFile = new File(filePath);
        if (!mPrintFile.exists()) {
            throw new FileNotFoundException("Not a invalid path!");
        }

        mDelayTime = DEFAULT_DELAY_TIME;
        mSubmitTime = System.currentTimeMillis() + mDelayTime;
        mCurrentStatus = TaskStatus.TOPRINT;
    }

    /* Get and set methods */
    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public File getPrintFile() {
        return mPrintFile;
    }

    public void setPrintFile(File printFile) {
        mPrintFile = printFile;
    }

    public long getDelayTime() {
        return mDelayTime;
    }

    public void setDelayTime(long delayTime) {
        mDelayTime = delayTime;
        mSubmitTime += delayTime;
    }

    public long getSubmitTime() {
        return mSubmitTime;
    }

    public long getTimeOut() {
        return mTimeOut;
    }

    public void setTimeOut(long timeOut) {
        mTimeOut = timeOut;
    }

    public TaskStatus getCurrentStatus() {
        if(!checkInvalid()) {
            mCurrentStatus = TaskStatus.ABORTED;
        }
        return mCurrentStatus;
    }

    public TaskStatus getOldStatus() {
        return  mOldStatus;
    }

    /**
     * Update the task status
     * @param newStatus -> the status which will be given to mCurrentStatus
     */
    public void updateStatus(TaskStatus newStatus) {
        if(!checkInvalid()) {
            mOldStatus = mCurrentStatus = TaskStatus.ABORTED;
        } else {
            if(mCurrentStatus != newStatus) {
                mOldStatus = mCurrentStatus;
                mCurrentStatus = newStatus;
            }
        }
    }

    /**
     * Set the OnUpdateListener -> instant the onUpdate method
     * @param listener
     */
    public void setOnUpdateListener(OnUpdateListener listener) {
        mOnUpdateListener = listener;
    }

    @Override
    public int compareTo(PrintTask printTask) {
        if (getPriority() < printTask.getPriority()) {
            return -1;
        } else if (getPriority() == printTask.getPriority()) {
            if (getSubmitTime() < printTask.getSubmitTime()) {
                return -1;
            } else if (getSubmitTime() == printTask.getSubmitTime()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    /**
     * start print operation and update its status
     */
    public void start() {
        switch (getCurrentStatus()) {
            case TOPRINT: {
                boolean result =print();
                if(result) {
                    updateStatus(TaskStatus.SUCCESSFUL);
                } else {
                    updateStatus(TaskStatus.FAILED);
                }
                mOnUpdateListener.onUpdate();
                break;
            }
            case ABORTED:
                mOnUpdateListener.onUpdate();
                break;
            default:
                break;
        }
        System.out.println("Task " + this.toString());
    }

    /**
     * Print operation
     * @return true -> Successful, false -> failed
     */
    //TODO: actual action
    private synchronized boolean print()  {
        return Math.random() > 0.5;
    }

    /**
     * Check the task is out of date or not
     * @return true -> valid; false -> out of date
     */
    private boolean checkInvalid() {
        if (mTimeOut != INFINITE) {
            return System.currentTimeMillis() <= mTimeOut + mSubmitTime;
        } else {
            return true;
        }
    }

    public enum TaskStatus {
        ABORTED("Aborted"),
        TOPRINT("ToPrint"),
        SUCCESSFUL("Successful"),
        FAILED("Failed"),
        UNKNOWN("Unknown");

        private String mStatus;

        TaskStatus(String status) {
            mStatus = status;
        }

        @Override
        public String toString() {
            return  mStatus;
        }

        public static TaskStatus getStatus(String status) {
            TaskStatus taskStatus = UNKNOWN;
            switch (status) {
                case "Aborted":
                    taskStatus = ABORTED;
                    break;
                case "ToPrint":
                    taskStatus = TOPRINT;
                    break;
                case "Successful":
                    taskStatus = SUCCESSFUL;
                    break;
                case "Failed":
                    taskStatus = FAILED;
                    break;
                default:
                    break;
            }
            return taskStatus;
        }
    }

    public interface OnUpdateListener{
        void onUpdate();
    }

    @Override
    public String toString() {
        return mPrintFile.getName() + " - " + mCurrentStatus;
    }
}