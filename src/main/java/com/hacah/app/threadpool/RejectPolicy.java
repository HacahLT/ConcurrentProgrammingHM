package com.hacah.app.threadpool;

/**
 * 任务队列满了，拒绝策略
 *
 * @author Hacah
 * @date 2022/11/30 15:19
 */
public interface RejectPolicy<T> {
    /**
     * 策略方法
     *
     * @param queue 队列
     * @param task  任务
     */
    void reject(MyBlockingQueue<T> queue, T task);
}
