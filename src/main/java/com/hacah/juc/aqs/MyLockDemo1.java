package com.hacah.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自己使用AQS实现一个，不可重入的锁
 *
 * @author Hacah
 * @date 2022/12/9 13:47
 */
@Slf4j
public class MyLockDemo1 {

    public static void main(String[] args) {
        MyLock myLock = new MyLock();
        new Thread(() -> {
            myLock.lock();
            try {
                log.debug("输出1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("输出1");
            } finally {
                myLock.unlock();
            }
        }).start();
        new Thread(() -> {
            myLock.lock();
            try {
                log.debug("输出2");
                log.debug("输出2");
            } finally {
                myLock.unlock();
            }
        }).start();


    }


}

class MyLock implements Lock {

    private MySync mySync = new MySync();

    /**
     * 加锁，不成功加入阻塞队列
     */
    @Override
    public void lock() {
        mySync.acquire(1);
    }

    /**
     * 锁，可中断
     *
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        mySync.acquireSharedInterruptibly(1);
    }

    /**
     * 尝试锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        return mySync.tryAcquire(1);
    }

    /**
     * 超时锁
     *
     * @return
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mySync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /**
     * 解锁
     */
    @Override
    public void unlock() {
        mySync.release(1);
    }

    /**
     * 新增条件
     *
     * @return
     */
    @Override
    public Condition newCondition() {
        return mySync.newCondition();
    }

    static class MySync extends AbstractQueuedSynchronizer {
        /**
         * 获取锁,独占
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (arg == 1) {
                // 获取锁
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }
            return false;
        }

        /**
         * 释放锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (arg == 1) {
                if (getState() == 0) {
                    throw new IllegalMonitorStateException();
                }
                setExclusiveOwnerThread(null);
                setState(0);
                return true;
            }
            return false;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
    }
}
