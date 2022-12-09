package com.hacah.app.executor;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * ScheduledExecutorService使用
 *
 * @author Hacah
 * @date 2022/12/5 10:35
 */
@Slf4j(topic = "ScheduledExecutorServiceDemo")
public class ScheduledExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        // delayRun(scheduledExecutorService);
        // rep(scheduledExecutorService);
        // handleException();

        // 每周四，18点执行任务
        // 当前时间到周四的间隔
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thursdayTime = now.with(DayOfWeek.THURSDAY).withHour(18).withMinute(0).withNano(0).withSecond(0);
        if (thursdayTime.compareTo(now) < 0) {
            thursdayTime = thursdayTime.plusWeeks(1);
        }
        // 时间差
        long delayTime = Duration.between(now, thursdayTime).toMillis();
        log.debug("当前到周四16点间隔时间{}ms", delayTime);

        // 一周间隔的毫秒时间
        int per = 1000 * 60 * 60 * 24 * 7;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("每周四，18点执行任务");
        }, delayTime, per, TimeUnit.MILLISECONDS);

    }

    private static void handleException() {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<?> task1 = pool.submit(() -> {
            log.debug("task1");
            int i = 1 / 0;
        });
        try {
            task1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void rep(ScheduledExecutorService scheduledExecutorService) {

        // 重复执行任务，延迟一秒执行任务，并每隔一秒重复执行
        // 如果任务执行时间超过指定的重复时间，以执行时间为准，保证任务不重叠执行。
       /* scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.debug("任务执行。");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
         },1,1,TimeUnit.SECONDS);*/

        // 重复执行任务，任务间隔等于（任务时间+指定间隔时间）
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.debug("任务执行。");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private static void delayRun(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.schedule(() -> {
            log.debug("任务1");
            int i = 19 / 0;
        }, 1, TimeUnit.SECONDS);

        scheduledExecutorService.schedule(() -> {
            log.debug("任务2");
        }, 1, TimeUnit.SECONDS);
    }

}
