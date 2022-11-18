package com.hacah.sync.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition实现指定条件唤醒
 *
 * @author Hacah
 * @date 2022/11/16 9:53
 */
@Slf4j
public class ConditionDemo {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition conditionSmoke = lock.newCondition();
    private static Condition conditionBreakfast = lock.newCondition();
    private static Boolean hasSmoke = false;
    private static Boolean hasBreakfast = false;

    public static void main(String[] args) throws InterruptedException {

        // 不断等待烟过来
        new Thread(() -> {
            lock.lock();
            try {
                while (!hasSmoke) {
                    try {
                        log.debug("等待送烟的过来");
                        conditionSmoke.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到烟了");
            } finally {
                lock.unlock();
            }
        }).start();


        // 不断等待早餐过来
        new Thread(() -> {
            lock.lock();
            try {
                while (!hasBreakfast) {
                    try {
                        log.debug("等待送早餐的过来");
                        conditionBreakfast.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到早餐了");
            } finally {
                lock.unlock();
            }
        }).start();

        // 送早餐
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                hasBreakfast = true;
                conditionBreakfast.signal();
                log.debug("送早餐了");
            } finally {
                lock.unlock();
            }
        }, "送早餐");

        // 送烟
        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                hasSmoke = true;
                conditionSmoke.signal();
                log.debug("送烟了");
            } finally {
                lock.unlock();
            }
        }, "送烟");

        Thread.sleep(100);
        t1.start();
        t2.start();


    }

}
