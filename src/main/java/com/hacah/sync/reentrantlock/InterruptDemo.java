package com.hacah.sync.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可中断
 *
 * @author Hacah
 * @date 2022/11/15 11:09
 */
@Slf4j
public class InterruptDemo {
    public static void main(String[] args) {

        ReentrantLock reentrantLock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            try {
                reentrantLock.lockInterruptibly();
                log.debug("t1执行操作");
            } catch (InterruptedException e) {
                log.debug("t1线程中断");
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }, "t1");
        t1.start();

        reentrantLock.lock();
        try {
            log.debug("把t1中断");
            t1.interrupt();
        } finally {
            reentrantLock.unlock();
        }


    }


}
