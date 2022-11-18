package com.hacah.mode.syn.sequencecontrol;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * 需求：线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现
 * LockSupport
 * <p>
 * 三个线程需要设置个线程可执行。
 * 并且线程建立顺序
 *
 * @author Hacah
 * @date 2022/11/16 11:48
 */
public class Demo6 {

    public static void main(String[] args) throws InterruptedException {
        CtrSeqLockSupport ctrSeq = new CtrSeqLockSupport(5);
        Thread t1 = new Thread(() -> {
            ctrSeq.print("a");
        });
        Thread t2 = new Thread(() -> {
            ctrSeq.print("b");
        });
        Thread t3 = new Thread(() -> {
            ctrSeq.print("c");
        });
        ctrSeq.start(t1, t2, t3);

    }


}

class CtrSeqLockSupport {

    private Integer loopSize;
    private List<Thread> threadList = new Vector<>();

    public CtrSeqLockSupport(Integer loopSize) {
        this.loopSize = loopSize;
    }

    /**
     * 作为启动的方法
     *
     * @param
     */
    public void start(Thread... threadAy) {
        List<Thread> threads1 = Arrays.asList(threadAy);
        threadList.addAll(threads1);
        for (Thread thread : threadList) {
            thread.start();
        }
        Optional.ofNullable(threadList).map(threads -> threads.get(0)).ifPresent(thread -> LockSupport.unpark(thread));
    }

    public void print(String printStr) {
        for (int i = 0; i < loopSize; i++) {
            LockSupport.park();
            System.out.print(printStr);
            LockSupport.unpark(getNextThread());
        }
    }

    /**
     * 获取需要唤醒的下一个线程
     */
    public Thread getNextThread() {
        if (threadList.size() == 3) {
            throw new RuntimeException("线程长度不对");
        }
        Thread reThread = null;
        if (Thread.currentThread() == threadList.get(0)) {
            reThread = threadList.get(1);
        } else if (Thread.currentThread() == threadList.get(1)) {
            reThread = threadList.get(2);
        } else if (Thread.currentThread() == threadList.get(2)) {
            reThread = threadList.get(0);
        }
        return reThread;
    }


}
