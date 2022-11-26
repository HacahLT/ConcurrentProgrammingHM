package com.hacah.nolock.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 获取unsafe去实现属性的安全修改
 *
 * @author Hacah
 * @date 2022/11/26 12:09
 */
public class UnSafeDemo {

    public static void main(String[] args) throws InterruptedException {

        Account account = new Account(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        IntStream.range(0, 100).forEach(value -> {
            executorService.execute(() -> {
                account.decrease(10);
                countDownLatch.countDown();
            });
        });
        countDownLatch.await();
        System.out.println(account.getBalance());
        executorService.shutdown();
    }


}

class Account {
    private static Unsafe unsafe;
    private static Long fieldOffset;

    static {
        try {
            // 反射获取
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
            // 计算balance偏移量
            fieldOffset = unsafe.objectFieldOffset(Account.class.getDeclaredField("balance"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * unsafe对基本类型生效
     */
    private volatile int balance;

    public Account(Integer balance) {
        this.balance = balance;
    }

    public Integer getBalance() {
        return balance;
    }


    public void decrease(Integer integer) {
        if (integer.compareTo(0) < 0) {
            return;
        }
        // 目标对象， 域偏移量， 原值， 新值
        while (!unsafe.compareAndSwapInt(this, fieldOffset, balance, balance - integer)) {

        }
        ;
    }


}
