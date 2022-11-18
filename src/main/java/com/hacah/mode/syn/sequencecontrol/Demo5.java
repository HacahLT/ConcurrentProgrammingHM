package com.hacah.mode.syn.sequencecontrol;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 需求：线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现
 * ReentrantLock
 * <p>
 * 三个线程需要设置个线程可执行。
 * 并且线程建立顺序
 *
 * @author Hacah
 * @date 2022/11/16 11:48
 */
public class Demo5 {

    public static void main(String[] args) throws InterruptedException {
        CtrSeq ctrSeq = new CtrSeq(5);
        Condition condition1 = ctrSeq.newCondition();
        Condition condition2 = ctrSeq.newCondition();
        Condition condition3 = ctrSeq.newCondition();
        new Thread(() -> {
            ctrSeq.print(condition1, condition2, "a");
        }).start();
        new Thread(() -> {
            ctrSeq.print(condition2, condition3, "b");
        }).start();
        new Thread(() -> {
            ctrSeq.print(condition3, condition1, "c");
        }).start();
        ctrSeq.start(condition1);

    }


}

class CtrSeq extends ReentrantLock {

    private Integer loopSize;

    public CtrSeq(Integer loopSize) {
        this.loopSize = loopSize;
    }

    /**
     * 作为启动的方法
     *
     * @param condition
     */
    public void start(Condition condition) {
        this.lock();
        try {
            condition.signalAll();
        } finally {
            this.unlock();
        }
    }

    public void print(Condition thisCondition, Condition nextCondition, String printStr) {
        for (int i = 0; i < loopSize; i++) {
            this.lock();
            try {
                thisCondition.await();
                System.out.print(printStr);
                nextCondition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }

        }


    }


}
