package com.hacah.mode.syn.sequencecontrol;

/**
 * 需求：先打印线程1再打印线程2
 * 设计：用一个变量判断是否需要打印，并且两个线程之间做同步。
 *
 * @author Hacah
 * @date 2022/11/16 11:05
 */
public class Demo1 {

    private static Boolean seq = true;
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        // seq为true输出线程2


        new Thread(() -> {
            synchronized (lock) {
                while (seq) {
                    try {
                        lock.wait(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("1");
            }
        }, "线程1").start();


        Thread.sleep(1000);
        new Thread(() -> {
            synchronized (lock) {
                if (seq) {
                    System.out.println("2");
                    seq = false;
                    lock.notifyAll();
                }
            }
        }, "线程2").start();


    }

}
