package com.hacah.mode.syn.sequencecontrol;

/**
 * 需求：先打印线程1再打印线程2
 * 使用join实现
 *
 * @author Hacah
 * @date 2022/11/16 11:05
 */
public class Demo2 {

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("2");
        }, "线程2");

        Thread t1 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1");
        }, "线程1");
        t1.start();


        t2.start();

    }

}
