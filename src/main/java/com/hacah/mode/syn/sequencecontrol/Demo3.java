package com.hacah.mode.syn.sequencecontrol;

import java.util.concurrent.locks.LockSupport;

/**
 * 需求：先打印线程1再打印线程2<br>
 * 使用LockSupport实现
 *
 * @author Hacah
 * @date 2022/11/16 11:05
 */
public class Demo3 {


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            System.out.println("1");
        }, "线程1");
        Thread t2 = new Thread(() -> {
            System.out.println("2");
            LockSupport.unpark(t1);
        }, "线程2");


        t1.start();
        Thread.sleep(10);
        t2.start();

    }

}
