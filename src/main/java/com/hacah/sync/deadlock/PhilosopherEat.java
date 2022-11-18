package com.hacah.sync.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 哲学家就餐
 * 哲学家问题操作，有五个哲学家围坐在圆桌旁，每个人的左右边都有一个筷子，一共五个筷子。哲学家只负责睡觉和吃饭，吃完就睡一秒。吃饭时哲学家需要从左和右各拿一个筷子才能吃。
 *
 * @author Hacah
 * @date 2022/11/14 19:31
 */
public class PhilosopherEat {

    public static void main(String[] args) throws InterruptedException {

        Chopstick c1 = new Chopstick();
        Chopstick c2 = new Chopstick();
        Chopstick c3 = new Chopstick();
        Chopstick c4 = new Chopstick();
        Chopstick c5 = new Chopstick();
        Philosopher philosopher = new Philosopher(c1, c2, "p1");
        Philosopher philosopher2 = new Philosopher(c2, c3, "p2");
        Philosopher philosopher3 = new Philosopher(c3, c4, "p3");
        Philosopher philosopher4 = new Philosopher(c4, c5, "p4");
        // Philosopher philosopher5 = new Philosopher(c5, c1, "p5");
        // 修改获取锁的循序，解决死锁
        Philosopher philosopher5 = new Philosopher(c1, c5, "p5");
        // 哲学家吃东西
        new Thread(philosopher).start();
        TimeUnit.MILLISECONDS.sleep(100);
        new Thread(philosopher2).start();
        new Thread(philosopher3).start();
        new Thread(philosopher4).start();
        new Thread(philosopher5).start();


    }


}


class Chopstick {

}

@Slf4j
class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private String name;

    public Philosopher(Chopstick left, Chopstick right, String name) {
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
            synchronized (left) {
                log.debug("我是{}， 我拿到左筷子", name);
                synchronized (right) {
                    log.debug("我是{}， 我拿到右筷子", name);
                    this.eat();
                }
            }
        }
    }
}
