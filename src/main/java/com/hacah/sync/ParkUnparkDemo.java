package com.hacah.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

/**
 * @author Hacah
 * @date 2022/11/13 11:01
 */
@Slf4j
public class ParkUnparkDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");

        }, "t1");
        t1.start();

        sleep(1000);
        log.debug("unpark...");
        LockSupport.unpark(t1);


    }

}
