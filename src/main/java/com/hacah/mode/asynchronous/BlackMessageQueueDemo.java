package com.hacah.mode.asynchronous;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.stream.IntStream;

/**
 * 实现一个阻塞消息队列
 *
 * @author Hacah
 * @date 2022/11/12 15:28
 */
public class BlackMessageQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(8);
        IntStream.range(1, 11).forEach(value -> {
            new Thread(() -> messageQueue.put(String.format("消息%s", value)), "生产者-" + value).start();
        });
        Thread.sleep(100);
        IntStream.range(1, 3).forEach(value -> {
            new Thread(() -> {
                while (true) {
                    messageQueue.take();
                }
            }, "消费者-" + value).start();
        });

    }
}

@Slf4j
class MessageQueue {
    private final LinkedList<String> list = new LinkedList<>();
    private Integer capacity;

    public MessageQueue(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * 取消息
     *
     * @return
     */
    public String take() {
        // 检查是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，线程：{}等待", Thread.currentThread().getName());
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 取值
            String first = list.removeFirst();
            log.debug("线程：{}，消费消息：{}", Thread.currentThread().getName(), first);
            list.notifyAll();
            return first;
        }
    }

    public void put(String message) {
        synchronized (list) {
            // 检查是否大于容器大小
            while (this.capacity <= list.size()) {
                try {
                    log.debug("队列满了，线程：{}等待", Thread.currentThread().getName());
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(message);
            log.debug("线程：{}，添加消息", Thread.currentThread().getName());
            list.notifyAll();

        }

    }


}

