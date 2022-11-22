package com.hacah.mode.syn.balking;

/**
 * 犹豫模式
 * 一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做
 * 了，直接结束返回
 *
 * @author Hacah
 * @date 2022/11/18 10:43
 */
public class Demo1 {

    private static volatile Boolean check = false;

    public static void main(String[] args) throws InterruptedException {
        Demo1 demo1 = new Demo1();
        for (int i = 0; i < 10; i++) {
            demo1.startCheck();
        }
        Thread.sleep(5000);
        demo1.stop();

    }

    public void startCheck() {
        new Thread(() -> {
            setCheck();
        }).start();
    }

    public void setCheck() {
        if (check) {
            System.out.println("已经启动检查线程了1");
            return;
        }
        synchronized (this) {
            if (check) {
                System.out.println("已经启动检查线程了2");
                return;
            }
            System.out.println("启动检查线程");
            check = true;
        }
        while (check) {
            System.out.println("检查中...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        new Thread(() -> {
            if (check) {
                check = false;
                System.out.println("停止线程");
            }
        }).start();

    }

}
