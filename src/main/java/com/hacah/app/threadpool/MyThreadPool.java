package com.hacah.app.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 * 线程池维护多个线程，执行传入的任务。 能指定线程大小，线程空闲存活时间。
 *
 * @author Hacah
 * @date 2022/11/30 11:37
 */
@Slf4j(topic = "MyThreadPool")
public class MyThreadPool {

    /**
     * 核心数量
     */
    private Integer coreSize;

    private MyBlockingQueue<Runnable> queue;

    /**
     * 空闲线程超时
     */
    private long timeout;

    private TimeUnit timeUnit;

    private HashSet<Worker> workers = new HashSet<>();

    private RejectPolicy<Runnable> rejectPolicy;

    public MyThreadPool(long timeout, TimeUnit timeUnit, Integer coreSize, Integer queueSize, RejectPolicy<Runnable> rejectPolicy) {
        this.queue = new MyBlockingQueue<>(queueSize);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task) {
        // 池没满就创建线程执行
        // 满了就添加到队列等待执行
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增工作任务和执行线程，准备执行");
                workers.add(worker);
                worker.start();
                return;
            }
        }
        if (workers.size() >= coreSize) {
            /*
            线程池线程满了，可以做：
            1. 一直等
            2. 带超时等
            3.让调用者放弃任务执行
            4.让调用者抛出异常
            5.让调用者自己执行任务
             */
            // log.debug("新增任务到队列");
            // queue.put(task);
            queue.tryPut(task, rejectPolicy);
        }

    }


    class Worker extends Thread {

        /**
         * 任务
         */
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {

            try {
                do {
                    if (task != null) {
                        task.run();
                    }
                } while ((task = queue.poll(timeout, timeUnit)) != null);
            } finally {
                // 删除线程
                synchronized (workers) {
                    log.debug("删除线程:{}", Thread.currentThread().getName());
                    workers.remove(this);
                }
            }


        }
    }


}
