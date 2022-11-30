package com.hacah.app.threadpool;

import java.util.concurrent.TimeUnit;

/**
 * @author Hacah
 * @date 2022/11/29 11:39
 */
public class MainRun {

    public static void main(String[] args) throws InterruptedException {
        // testQueue(runnableMyBlockingQueue);

        MyThreadPool myThreadPool = new MyThreadPool(100, TimeUnit.SECONDS, 2, 1, (queue, task) -> {
            // 拒绝策略
            // 1. 一直等
            queue.put(task);
            // 2. 带超时等
            // queue.offer(task, 100, TimeUnit.MILLISECONDS);
            // 3.让调用者放弃任务执行
            // System.out.println("放弃执行：" + task);
            // 4.让调用者抛出异常
            // throw new RuntimeException("任务执行失败，队列满："+task);
            // 5.让调用者自己执行任务
            // task.run();
        });
        myThreadPool.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "线程执行任务1");

        });
        myThreadPool.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "线程执行任务2");
        });
        myThreadPool.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "线程执行任务3");
        });
        myThreadPool.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "线程执行任务4");
        });

    }

    private static void testQueue() {
        MyBlockingQueue<Runnable> runnableMyBlockingQueue = new MyBlockingQueue<Runnable>(2);
        runnableMyBlockingQueue.put(() -> {
            System.out.println("1");
        });
        runnableMyBlockingQueue.put(() -> {
            System.out.println("1");
        });

        // new Thread(()->{
        //     runnableMyBlockingQueue.put(() -> {
        //         System.out.println("1");
        //     });
        // }).start();
        new Thread(() -> {
            runnableMyBlockingQueue.take();
        }).start();
        new Thread(() -> {
            runnableMyBlockingQueue.take();
        }).start();

        new Thread(() -> {
            runnableMyBlockingQueue.poll(1, TimeUnit.MILLISECONDS);
        }).start();


        // Thread.sleep(10000);
    }
}
