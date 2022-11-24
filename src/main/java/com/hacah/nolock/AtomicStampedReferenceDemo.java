package com.hacah.nolock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Hacah
 * @date 2022/11/23 11:32
 */
@Slf4j
public class AtomicStampedReferenceDemo {

    static AtomicStampedReference<String> ref = new AtomicStampedReference("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        // 这个共享变量被它线程修改过？
        String prev = ref.getReference();
        int stamp = ref.getStamp();
        log.debug("当前的版本是{}", stamp);
        other();
        Thread.sleep(1000);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", ref.getStamp(), ref.getStamp() + 1));
            log.debug("当前的版本是{}", ref.getStamp());
        }, "t1").start();
        Thread.sleep(500);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), ref.getStamp() + 1));
            log.debug("当前的版本是{}", ref.getStamp());
        }, "t2").start();
    }

}
