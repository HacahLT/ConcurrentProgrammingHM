package com.hacah.mode.syn.desiofmode;

import lombok.extern.slf4j.Slf4j;

/**
 * 用在一个线程等待另一个线程的执行结果。
 * 要点
 * 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject
 * 如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
 * JDK 中，join 的实现、Future 的实现，采用的就是此模式
 * 因为要等待另一方的结果，因此归类到同步模式
 *
 * @author Hacah
 * @date 2022/11/10 9:51
 */
@Slf4j
public class ProtectionPauseDemo {
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();


        new Thread(() -> {
            log.debug("等待线程下载数据");
            String response = guardedObject.getResponse(2000);
            if (response == null) {
                log.debug("等待时间到了，数据还没下载完直接跳出");
            } else {
                log.debug("获取数据完成，数据是{}", response);
            }
        }, "t1").start();


        new Thread(() -> {
            log.debug("正在下载数据");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guardedObject.setResponse("Data");
            // guardedObject.setResponse(null);
            log.debug("下载数据完成");
        }, "t2").start();


    }


}


class GuardedObject {

    private String response;


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
