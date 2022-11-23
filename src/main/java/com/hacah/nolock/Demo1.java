package com.hacah.nolock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

interface Account {
    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance()
                + " cost: " + (end - start) / 1000_000 + " ms");
    }

    // 获取余额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);
}

/**
 * 无锁方式解决线程问题
 *
 * @author Hacah
 * @date 2022/11/22 10:23
 */
public class Demo1 implements Account {

    private AtomicInteger balance;

    public Demo1(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    public static void main(String[] args) {

        Demo1 demo1 = new Demo1(10000);
        Account.demo(demo1);

    }

    @Override
    public Integer getBalance() {
        return this.balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true) {
            int b = balance.get();
            int result = b - amount;
            if (balance.compareAndSet(b, result)) {
                break;
            }
        }

    }

}
