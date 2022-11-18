package com.hacah.sync.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * 公平锁
 *
 * @author Hacah
 * @date 2022/11/16 9:28
 */
@Slf4j
public class FairLockDemo {

    public static void main(String[] args) throws InterruptedException {

        ReentrantLock reentrantLock = new ReentrantLock(true);
        IntStream.range(0, 500).forEach(value -> {
            new Thread(() -> {
                reentrantLock.lock();
                try {
                    log.debug("我一直在输出");
                } finally {
                    reentrantLock.unlock();
                }

            }).start();
        });
        Thread thread = new Thread(() -> {
            reentrantLock.lock();
            try {
                log.debug("插入数据");
            } finally {
                reentrantLock.unlock();
            }
        });

        thread.start();


    }

}
