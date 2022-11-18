package com.hacah.sync.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入
 *
 * @author Hacah
 * @date 2022/11/15 11:01
 */
@Slf4j
public class ReentrantDemo {

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> {
            m1();
        }).start();

    }

    public static void m1() {
        reentrantLock.lock();
        try {
            log.debug("执行m1方法");
            m2();
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void m2() {
        reentrantLock.lock();
        try {
            log.debug("执行m2方法");
        } finally {
            reentrantLock.unlock();
        }
    }


}
