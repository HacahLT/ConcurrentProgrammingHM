package com.hacah.app.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 * 实现一个线程池，这是线程池使用一个阻塞队列
 *
 * @author Hacah
 * @date 2022/11/29 11:17
 */
@Slf4j(topic = "MyBlockingQueue")
public class MyBlockingQueue<T> {

    /**
     * 队列长度
     */
    private volatile Integer queueSize;

    /**
     * 锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 存放数据
     */
    private LinkedList<T> queue = new LinkedList();

    /**
     * 生产者条件
     */
    private Condition producerCondition = lock.newCondition();

    /**
     * 消费者条件
     */
    private Condition comCondition = lock.newCondition();


    public MyBlockingQueue(Integer queueSize) {
        this.queueSize = queueSize;
    }

    /**
     * 添加任务到队列
     *
     * @param task
     * @return
     */
    public boolean put(T task) {
        if (task == null) {
            return false;
        }
        lock.lock();
        try {
            while (queue.size() == queueSize) {
                // 满队列
                try {
                    log.debug("队列满了，正在等待空位添加");
                    producerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.debug("添加任务成功");
            comCondition.signal();
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 添加任务到队列,带超时
     *
     * @param task
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        if (task == null) {
            return false;
        }
        lock.lock();
        try {
            long toNanos = timeUnit.toNanos(timeout);
            while (queue.size() == queueSize) {
                // 满队列
                try {
                    if (toNanos <= 0) {
                        return false;
                    }
                    log.debug("队列满了，正在等待空位添加");
                    toNanos = producerCondition.awaitNanos(toNanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.debug("添加任务成功");
            comCondition.signal();
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 获取队列
     *
     * @return
     */
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    log.debug("队列为空，正在等待有值后获取");
                    comCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T first = queue.removeFirst();
            log.debug("获取任务成功");
            producerCondition.signal();
            return first;
        } finally {
            lock.unlock();
        }

    }

    /**
     * 获取任务，可超时
     *
     * @param timeout
     * @param timeUnit
     * @return
     */
    public T poll(long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        log.error("获取任务超时:{}", Thread.currentThread().getName());
                        return null;
                    }
                    log.debug("队列为空，正在等待有值后获取");
                    // awaitNanos返回剩余时间纳秒
                    nanos = comCondition.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T first = queue.removeFirst();
            log.debug("获取任务成功");
            producerCondition.signal();
            return first;
        } finally {
            lock.unlock();
        }
    }

    public Integer getSize() {
        return queueSize;
    }


    /**
     * 尝试放入队列，满队列按照不同拒绝策略
     *
     * @param task
     * @param rejectPolicy
     */
    public void tryPut(T task, RejectPolicy<T> rejectPolicy) {
        if (task == null) {
            throw new RuntimeException();
        }
        lock.lock();
        try {
            if (queue.size() >= queueSize) {
                // 按照拒绝策略执行
                rejectPolicy.reject(this, task);
            } else {
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                comCondition.signal();
            }
        } finally {
            lock.unlock();
        }

    }
}
