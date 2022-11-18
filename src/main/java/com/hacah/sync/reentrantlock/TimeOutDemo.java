package com.hacah.sync.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁超时
 *
 * @author Hacah
 * @date 2022/11/15 11:22
 */
@Slf4j
public class TimeOutDemo {

    public static void main(String[] args) throws InterruptedException {

        ReentrantLock reentrantLock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            boolean b = false;
            // boolean b = reentrantLock.tryLock();
            try {
                b = reentrantLock.tryLock(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!b) {
                log.debug("没有获取锁，直接退出");
                return;
            }
            try {
                log.debug("t1执行操作");
            } finally {
                reentrantLock.unlock();
            }
        }, "t1");


        reentrantLock.lock();
        try {
            t1.start();
            Thread.sleep(2000);
        } finally {
            reentrantLock.unlock();
        }


    }

}
