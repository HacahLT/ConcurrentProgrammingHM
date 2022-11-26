package com.hacah.nolock.field;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 字段更新器,AtomicIntegerFieldUpdater使用
 *
 * @author Hacah
 * @date 2022/11/24 10:40
 */
public class Demo1 {


    public static void main(String[] args) throws InterruptedException {

        AtomicIntegerFieldUpdater<TestField> field1 = AtomicIntegerFieldUpdater.newUpdater(TestField.class, "field1");

        TestField testField = new TestField();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                field1.getAndIncrement(testField);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println(testField.getField1());


    }
}

class TestField {
    public volatile int field1;

    public int getField1() {
        return field1;
    }
}
