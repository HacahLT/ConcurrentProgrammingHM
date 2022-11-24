package com.hacah.nolock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author Hacah
 * @date 2022/11/23 11:50
 */
@Slf4j
public class AtomicMarkableReferenceDemo {
    static AtomicMarkableReference<String> ref = new AtomicMarkableReference("A", false);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        // 这个共享变量被它线程修改过？
        String prev = ref.getReference();
        log.debug("当前的标记是{}", ref.isMarked());
        other();
        Thread.sleep(1000);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", false, true));
    }

    private static void other() throws InterruptedException {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", false, true));
            log.debug("当前的标记是{}", ref.isMarked());
        }, "t1").start();
        Thread.sleep(500);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", true, true));
            log.debug("当前的标记是{}", ref.isMarked());
        }, "t2").start();
    }
}
