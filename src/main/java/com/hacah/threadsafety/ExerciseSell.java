package com.hacah.threadsafety;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.IntStream;

/**
 * 买票，把并发问题解决
 *
 * @author Hacah
 * @date 2022/11/5 23:22
 */
@Slf4j(topic = "test")
public class ExerciseSell {


    // Random 为线程安全
    static Random random = new Random();

    public static void main(String[] args) {
        ExerciseSell exerciseSell = new ExerciseSell();
        IntStream.range(1, 11).forEach(value -> {
            System.out.println(value);
            exerciseSell.run();
        });
    }

    // 随机 1~5
    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }

    public void run() {
        TicketWindow ticketWindow = new TicketWindow(2000);
        List<Thread> list = new ArrayList<>();
        // 用来存储买出去多少张票
        List<Integer> sellCount = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread t = new Thread(() -> {
                // 分析这里的竞态条件
                int count = ticketWindow.sell(randomAmount());
                sellCount.add(count);
            });
            list.add(t);
            t.start();
        }
        list.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 买出去的票求和
        log.debug("selled count:{}", sellCount.stream().mapToInt(c -> c).sum());
        // 剩余票数
        log.debug("remainder count:{}", ticketWindow.getCount());
    }

}

class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int sell(int amount) {
        // if (this.count >= amount) {
        //     this.count -= amount;
        //     return amount;
        // } else {
        //     return 0;
        // }

        // 加上同步
        synchronized (this) {
            if (this.count >= amount) {
                this.count -= amount;
                return amount;
            } else {
                return 0;
            }
        }

    }
}
