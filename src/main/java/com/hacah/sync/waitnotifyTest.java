package com.hacah.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Hacah
 * @date 2022/11/9 9:47
 */
@Slf4j
public class waitnotifyTest {

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();

        new Thread(() -> {
            synchronized (lock) {
                try {
                    log.debug("{}阻塞了", Thread.currentThread().getName());
                    lock.wait();
                    log.debug("{}被唤醒了", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();


        new Thread(() -> {
            synchronized (lock) {
                try {
                    log.debug("{}阻塞了", Thread.currentThread().getName());
                    lock.wait();
                    log.debug("{}被唤醒了", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();


        new Thread(() -> {
            synchronized (lock) {
                try {
                    log.debug("{}阻塞了", Thread.currentThread().getName());
                    lock.wait(1000);
                    log.debug("{}唤醒了", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t3").start();


        TimeUnit.MILLISECONDS.sleep(100);
        synchronized (lock) {
            lock.notify();
            log.debug("唤醒一个线程");
        }

        // synchronized (lock){
        //     lock.notifyAll();
        //     log.debug("唤醒全部线程");
        // }

    }
}
