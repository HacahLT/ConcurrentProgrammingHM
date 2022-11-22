package com.hacah.memory;

/**
 * 可见性
 *
 * @author Hacah
 * @date 2022/11/18 10:03
 */
public class VisualableDemo {

    static boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            while (run) {
                // ....
            }
        });
        t.start();
        Thread.sleep(1);
        run = false; // 线程t不会如预想的停下来
    }


}
