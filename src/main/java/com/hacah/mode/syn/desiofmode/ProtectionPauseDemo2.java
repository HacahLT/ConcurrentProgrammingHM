package com.hacah.mode.syn.desiofmode;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 多线程的暂停性保护
 *
 * @author Hacah
 * @date 2022/11/12 13:37
 */
public class ProtectionPauseDemo2 {
    public static void main(String[] args) throws InterruptedException {
        IntStream.range(1, 11).forEach(value -> {
            People people = new People();
            new Thread(people, "寄件人").start();
        });
        Thread.sleep(1000);
        Set<Integer> allIds = ExpressBoxes.getAllIds();
        List<Integer> ids = new ArrayList<>(allIds);
        for (Integer id : ids) {
            Courier courier = new Courier(id);
            new Thread(courier, "快递员-" + id).start();
        }


    }

}

@Slf4j
class People implements Runnable {

    @Override
    public void run() {
        ExpressBoxes expressBoxes = new ExpressBoxes();
        GuardedObject2 exp = expressBoxes.createExp();
        exp.setResponse(String.format("这是%s的快递, 快递编号：%d", Thread.currentThread().getName(), exp.getId()));
        log.debug("我是{}，已经寄出快递, 快递编号：{}", Thread.currentThread().getName(), exp.getId());
    }
}

@Slf4j
class Courier implements Runnable {

    /**
     * 要寄出的id
     */
    private Integer id;

    public Courier(Integer id) {
        this.id = id;
    }

    @Override
    public void run() {
        ExpressBoxes expressBoxes = new ExpressBoxes();
        GuardedObject2 exp = expressBoxes.getExp(id);
        log.debug("我是{}，正在取出快递, 取id是{}的快递", Thread.currentThread().getName(), id);
        Optional.ofNullable(exp).ifPresent(guardedObject2 -> {
            String response = exp.getResponse(5000);
            if (response == null) {
                log.debug("我是{}，没有快递可取", Thread.currentThread().getName());
            } else {
                log.debug("我是{}，取出快递了，快递是：{}", Thread.currentThread().getName(), response);
            }

        });

    }
}


class ExpressBoxes {
    private static Hashtable<Integer, GuardedObject2> express = new Hashtable<>();

    private static volatile Integer i = 0;

    public static synchronized Integer inc() {
        return ++i;
    }

    public static Set<Integer> getAllIds() {
        Set<Integer> integers = express.keySet();
        return integers;
    }

    /**
     * 创建一个快递
     *
     * @return
     */
    public GuardedObject2 createExp() {
        Integer inc = inc();
        GuardedObject2 guardedObject2 = new GuardedObject2(inc);
        express.put(inc, guardedObject2);
        return guardedObject2;
    }

    public GuardedObject2 getExp(Integer id) {
        return express.remove(id);
    }


}


class GuardedObject2 {

    private String response;

    private Integer id;

    public GuardedObject2(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    /**
     * 获取数据
     *
     * @param time 等待时间
     * @return
     */
    public String getResponse(long time) {
        synchronized (this) {
            long startTime = System.currentTimeMillis();
            // 经历的时间
            long passTime = 0;
            while (this.response == null) {
                if (time <= passTime) {
                    break;
                }
                // 剩余等待时间
                long lastTime = time - passTime;
                try {
                    this.wait(lastTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passTime = System.currentTimeMillis() - startTime;
            }
            return response;
        }
    }


    public void setResponse(String response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }

}
