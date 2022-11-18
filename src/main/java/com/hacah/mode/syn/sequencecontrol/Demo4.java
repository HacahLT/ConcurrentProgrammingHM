package com.hacah.mode.syn.sequencecontrol;

/**
 * 需求：线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现
 * wait/notify
 * <p>
 * 三个线程需要设置标志某个线程可执行。
 * 并且需要标记那个线程之后执行，建立顺序
 *
 * @author Hacah
 * @date 2022/11/16 11:48
 */
public class Demo4 {

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyCtrSeq waitNotifyCtrSeq = new WaitNotifyCtrSeq(1, 5);
        new Thread(() -> {
            waitNotifyCtrSeq.print(1, 2, "a");
        }).start();
        new Thread(() -> {
            waitNotifyCtrSeq.print(2, 3, "b");
        }).start();
        new Thread(() -> {
            waitNotifyCtrSeq.print(3, 1, "c");
        }).start();


    }


}

class WaitNotifyCtrSeq {

    /**
     * 标志某个线程可执行
     * 1：线程1可执行
     * 2：线程2可执行
     * 3：线程3可执行
     */
    private Integer flag;

    private Integer loopSize;


    public WaitNotifyCtrSeq(Integer flag, Integer loopSize) {
        this.flag = flag;
        this.loopSize = loopSize;
    }

    public void print(Integer threadFlag, Integer nextFlag, String printStr) {
        for (int i = 0; i < loopSize; i++) {
            synchronized (this) {
                while (!flag.equals(threadFlag)) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(printStr);
                // 设置下一个唤醒的线程
                flag = nextFlag;
                this.notifyAll();
            }
        }


    }


}
