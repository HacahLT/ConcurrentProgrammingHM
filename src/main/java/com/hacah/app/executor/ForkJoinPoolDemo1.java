package com.hacah.app.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * ForkJoinPool案例
 * 实现1-n的整数和
 *
 * @author Hacah
 * @date 2022/12/7 11:42
 */
public class ForkJoinPoolDemo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // ForkJoinPool forkJoinPool = new ForkJoinPool();
        // ForkJoinTask<Integer> submit = forkJoinPool.submit(new AddTask(5));
        // System.out.println(submit.get());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> submit = forkJoinPool.submit(new AddTask2(1, 100000));
        System.out.println(submit.get());
    }

}

/**
 * 单支地去划分小任务
 */
class AddTask extends RecursiveTask<Integer> {

    /**
     * 1-最大值，sum为最大值
     */
    private Integer sum;

    public AddTask(Integer sum) {
        this.sum = sum;
    }

    @Override
    protected Integer compute() {
        if (sum == 1) {
            return 1;
        }

        AddTask nextTask = new AddTask(sum - 1);
        nextTask.fork();

        return sum + nextTask.join();
    }
}

/**
 * 优化，使用双支划分
 * 初始值-最终值的之间整数和
 */
class AddTask2 extends RecursiveTask<Integer> {

    /**
     * 初始值
     */
    private Integer begin;

    /**
     * 最终值
     */
    private Integer end;

    public AddTask2(Integer begin, Integer end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * 优化：每份一百份计算一次。最后不够也计算
     *
     * @return
     */
    @Override
    protected Integer compute() {
        if (100 > (end - begin)) {
            int sum = IntStream.range(begin, end + 1).reduce((left, right) -> left + right).getAsInt();
            return sum;
        }
        int i = (begin + end) / 2;

        AddTask2 begTask = new AddTask2(begin, i);
        AddTask2 endTask = new AddTask2(i + 1, end);

        begTask.fork();
        endTask.fork();

        return begTask.join() + endTask.join();
    }
    // @Override
    // protected Integer compute() {
    //     if (begin.equals(end)) {
    //         return begin;
    //     }else if (end - begin == 1) {
    //         return end + begin;
    //     }
    //
    //     int i = (begin + end) / 2;
    //
    //     AddTask2 begTask = new AddTask2(begin, i);
    //     AddTask2 endTask = new AddTask2(i + 1, end);
    //
    //     begTask.fork();
    //     endTask.fork();
    //
    //     return begTask.join()+endTask.join();
    // }


}
