package com.hacah.sync.deadlock;

import lombok.extern.slf4j.Slf4j;

/**
 * 两个线程相互获取资源
 *
 * @author Hacah
 * @date 2022/11/14 17:05
 */
@Slf4j
public class Demo {

    public static void main(String[] args) {

        Object lock1 = new Object();
        Object lock2 = new Object();


        new Thread(() -> {
            synchronized (lock2) {
                log.debug("线程：{}， 获取锁lock2了", Thread.currentThread().getName());
                log.debug("线程：{}， 正在获取lock1", Thread.currentThread().getName());
                synchronized (lock1) {
                    log.debug("线程：{}， 获取锁lock1了", Thread.currentThread().getName());
                }
            }
        }, "t1").start();


        new Thread(() -> {
            synchronized (lock1) {
                log.debug("线程：{}， 获取锁lock1了", Thread.currentThread().getName());
                log.debug("线程：{}， 正在获取lock2", Thread.currentThread().getName());
                synchronized (lock2) {
                    log.debug("线程：{}， 获取锁lock2了", Thread.currentThread().getName());
                }
            }
        }, "t2").start();


    }


}
