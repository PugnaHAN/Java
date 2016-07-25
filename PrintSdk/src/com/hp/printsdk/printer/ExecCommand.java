package com.hp.printsdk.printer;

import java.io.*;

/**
 * Created by zhangjuh on 2016/7/4.
 */
public class ExecCommand {
    private static final String TAG = ExecCommand.class.getSimpleName();

    private static final String CMD_SU = "su";
    private static final String CMD_SH = "sh";
    private static final String CMD_EXIT = "eixt\n";
    private static final String CMD_LE = "\n";

    private ExeResult mResult;
    private String mCommand;

    public ExecCommand(String cmd) {
        mCommand = cmd;
    }

    public ExecCommand() {
        mCommand = null;
    }

    public ExeResult getResult() {
        return mResult;
    }

    public String getCommand() {
        return mCommand;
    }

    public void setCommand(String command) {
        mCommand = command;
    }

    public static class ExeResult {
        private int mResult = -1;
        private String mSuccessResult;
        private String mFailedReulst;

        public int getResult() {
            return mResult;
        }

        public void setResult(int result) {
            mResult = result;
        }

        public String getSuccessResult() {
            return mSuccessResult;
        }

        public void setSuccessResult(String successResult) {
            mSuccessResult = successResult;
        }

        public String getFailedReulst() {
            return mFailedReulst;
        }

        public void setFailedReulst(String failedReulst) {
            mFailedReulst = failedReulst;
        }
    }

    public static String execute(String command) {
        StringBuilder sb = new StringBuilder();
        if(command != null) {
            try {
                Process process = Runtime.getRuntime().exec(command);
                InputStream is = process.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is), 4096);
                String line;
                while((line = br.readLine()) != null) {
                    sb.append("\r\n");
                    sb.append(line);
                }
            } catch (IOException e) {
                sb.append(e.getMessage());
            }
        }
        return sb.toString();
    }

    public static ExeResult exec(String command) {
        ExeResult result = new ExeResult();

        Process process = null;
        OutputStream os = null;
        DataOutputStream dos = null;

        try{
            process = Runtime.getRuntime().exec(CMD_SU);
            os = process.getOutputStream();
            dos = new DataOutputStream(os);

            // Log.d(TAG, "command is " + command);
            dos.writeBytes(command);
            dos.flush();
            dos.writeBytes(CMD_EXIT);

            int procResult = process.waitFor();
            result.setResult(procResult);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
