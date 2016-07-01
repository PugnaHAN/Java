package com.hp.printsdk;

import java.io.FileNotFoundException;

/**
 * Created by zhangjuh on 2016/6/30.
 */
public class TestSample {

    public static void main(String[] args) {
        try {
            PrintTaskManager manager = PrintTaskManager.getInstance();

            PrintTask task1 = new PrintTask(2,
                    "D:\\Work\\Projects\\Java\\PrintSdk\\texts\\TEST_PRIORITY_2.txt");
            PrintTask task2 = new PrintTask(0,
                    "D:\\Work\\Projects\\Java\\PrintSdk\\texts\\TEST_PRIORITY_0.txt");
            task2.setTimeOut(100);
            PrintTask task3 = new PrintTask(5,
                    "D:\\Work\\Projects\\Java\\PrintSdk\\texts\\TEST_PRIORITY_5.txt");
            task3.setTimeOut(1000);
            PrintTask task4 = new PrintTask(
                    "D:\\Work\\Projects\\Java\\PrintSdk\\texts\\TEST_PRIORITY_2_WITH_DELAY.txt");
            task4.setPriority(2);
            task4.setDelayTime(1000);

            manager.add(task1);
            manager.add(task2);
            manager.add(task3);
            manager.add(task4);

            TaskManagerThread thread1 = new TaskManagerThread(
                    ()-> {
                        while (manager.getToPrintQueue().size() > 0) {
                            try{
                                Thread.sleep(1000);
                                System.out.println(Thread.currentThread().getName());
                                manager.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

            Thread thread = new Thread(() -> {
                while (manager.getToPrintQueue().size() > 0) {
                    try{
                        Thread.sleep(1000);
                        manager.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread1.start();
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
