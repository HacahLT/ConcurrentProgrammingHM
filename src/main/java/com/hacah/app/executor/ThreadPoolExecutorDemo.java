package com.hacah.app.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * ThreadPoolExecutor使用
 *
 * @author Hacah
 * @date 2022/12/2 10:41
 */
@Slf4j
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<String> runnable2 = () -> {
            log.debug("执行任务 2");
            Thread.sleep(500);
            log.debug("结束任务 2");
            return "任务2";
        };


        Callable<String> runnable3 = () -> {
            log.debug("执行任务 3");
            Thread.sleep(3000);
            log.debug("结束任务 3");
            return "任务3";
        };


        // runThread();
        executorService.submit(runnable2);
        executorService.submit(runnable3);

        // 关闭
        log.debug("shutdown");
        executorService.shutdown();
        // List<Runnable> runnables = executorService.shutdownNow();
        // log.debug("剩余的任务:{}",runnables);

        // executorService.awaitTermination(5, TimeUnit.SECONDS);
        log.debug("调用shutdown结束线程池了");
        executorService.submit(() -> {
            log.debug("执行任务 4");
            log.debug("结束任务 4");
        });


    }

    /**
     * 执行方法
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private static void runThread() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable runnable = () -> {
            log.debug("执行任务 1");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("任务1");
            log.debug("结束任务 1");
        };
        Callable<String> runnable2 = () -> {
            log.debug("执行任务 2");
            Thread.sleep(500);
            log.debug("结束任务 2");
            return "任务2";
        };


        Callable<String> runnable3 = () -> {
            log.debug("执行任务 3");
            Thread.sleep(100);
            log.debug("结束任务 3");
            return "任务3";
        };

        // 执行run
        executorService.execute(runnable);

        // 取返回值
        Future<String> submit = executorService.submit(runnable2);
        String s = (String) submit.get();
        log.debug(s);
        System.out.println("--------------------------------------------------------------------");

        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(runnable2, runnable3));
        for (Future<String> future : futures) {
            log.debug(future.get());
        }
        System.out.println("--------------------------------------------------------------------");

        String s1 = executorService.invokeAny(Arrays.asList(runnable2, runnable3), 100, TimeUnit.MINUTES);
        log.debug(s1);
    }

}
