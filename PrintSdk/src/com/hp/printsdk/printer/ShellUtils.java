package com.hp.printsdk.printer;

import java.io.*;
import java.util.List;

/**
 * ShellUtils
 * <ul>
 * <strong>Check root</strong>
 * </ul>
 * <ul>
 * <strong>Execte command</strong>
 * <li>{@link ShellUtils#execCommand(String, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String, boolean, boolean)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
 */
public class ShellUtils {

    private ShellUtils() {
        throw new AssertionError();
    }

    /**
     * execute shell command, default return result msg
     *
     * @param command command
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String, boolean, boolean)
     */
    public static String execCommand(String command, boolean isRoot) {
        return execCommand(command, isRoot, true);
    }

    /**
     * execute shell commands
     *
     * @param command command array
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return int
     */
    public static String execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (command == null || command.length() == 0) {
            return null;
        }

        Process process = null;
        BufferedReader input = null;
        StringBuilder exeResult = new StringBuilder();
        InputStream is = null;

        try {
            // TODO - To be modified
            process = Runtime.getRuntime().exec(command);
            is = process.getInputStream();
            input = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = input.readLine()) != null) {
                exeResult.append(line);
            }

            result = process.waitFor();
            // get command result
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }

                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }

        if(result != -1) {
            return exeResult.toString();
        } else {
            return null;
        }
    }
}
