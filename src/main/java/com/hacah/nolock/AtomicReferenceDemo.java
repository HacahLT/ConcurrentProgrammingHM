package com.hacah.nolock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

interface Account2 {
    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account2 account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(new BigDecimal(10));
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
    BigDecimal getBalance();

    // 取款
    void withdraw(BigDecimal amount);
}

/**
 * @author Hacah
 * @date 2022/11/23 11:01
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        AccountImpl account = new AccountImpl(new BigDecimal(10000));
        Account2.demo(account);
    }
}

class AccountImpl implements Account2 {

    private AtomicReference<BigDecimal> balance;

    public AccountImpl(BigDecimal balance) {
        this.balance = new AtomicReference(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        BigDecimal prv = null;
        BigDecimal next = null;
        do {
            prv = balance.get();
            next = prv.subtract(amount);
        } while (!balance.compareAndSet(prv, next));
    }

}


