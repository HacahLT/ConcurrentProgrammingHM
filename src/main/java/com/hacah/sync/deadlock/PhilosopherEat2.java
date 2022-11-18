package com.hacah.sync.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock解决死锁问题
 *
 * @author Hacah
 * @date 2022/11/15 11:31
 */
public class PhilosopherEat2 {

    public static void main(String[] args) {
        ReentrantLock c1 = new ReentrantLock();
        ReentrantLock c2 = new ReentrantLock();
        ReentrantLock c3 = new ReentrantLock();
        ReentrantLock c4 = new ReentrantLock();
        ReentrantLock c5 = new ReentrantLock();
        Philosopher2 philosopher = new Philosopher2(c1, c2, "p1");
        Philosopher2 philosopher2 = new Philosopher2(c2, c3, "p2");
        Philosopher2 philosopher3 = new Philosopher2(c3, c4, "p3");
        Philosopher2 philosopher4 = new Philosopher2(c4, c5, "p4");
        Philosopher2 philosopher5 = new Philosopher2(c5, c1, "p5");
        philosopher.start();
        philosopher2.start();
        philosopher3.start();
        philosopher4.start();
        philosopher5.start();
    }

}


@Slf4j
class Philosopher2 extends Thread {

    private ReentrantLock left;
    private ReentrantLock right;
    private String name;

    public Philosopher2(ReentrantLock left, ReentrantLock right, String name) {
        this.left = left;
        this.right = right;
        this.name = name;
    }

    public void eat() {
        log.debug("我是{}，吃东西了", name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (left.tryLock()) {
                try {
                    log.debug("我是{}， 我拿到左筷子", name);
                    if (right.tryLock()) {
                        try {
                            log.debug("我是{}， 我拿到右筷子", name);
                            this.eat();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
        }
    }
}
