package com.hacah.threadsafety;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 转账
 *
 * @author Hacah
 * @date 2022/11/6 0:34
 */
@Slf4j(topic = "test")
public class ExerciseTransfer {

    // Random 为线程安全
    static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transferMoney(b, randomAmount());
            }
        }, "A");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transferMoney(a, randomAmount());
            }
        }, "B");

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        log.debug("总共有多少钱：{}", a.getMoney() + b.getMoney());


    }

    // 随机 1~100
    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }


}


class Account {

    private Integer money;


    public Account(Integer money) {
        this.money = money;
    }

    public Integer getMoney() {
        return this.money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public void transferMoney(Account target, Integer amount) {
        synchronized (Account.class) {
            if (this.money > amount) {
                this.money -= amount;
                target.setMoney(target.getMoney() + amount);
            }
        }
    }


}


