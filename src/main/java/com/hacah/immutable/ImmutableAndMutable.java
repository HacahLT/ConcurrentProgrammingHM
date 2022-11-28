package com.hacah.immutable;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * 不可变类与可变类例子
 *
 * @author Hacah
 * @date 2022/11/27 14:48
 */
@Slf4j
public class ImmutableAndMutable {

    public static void main(String[] args) {

        // immut();
        // mutable();

        final BigDecimal[] bigDecimal = {new BigDecimal(10)};
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                bigDecimal[0] = bigDecimal[0].add(BigDecimal.valueOf(1));
            }).start();
        }
        System.out.println(bigDecimal[0]);

    }

    /**
     * 不可变类，线程安全例子
     */
    private static void immut() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor date = dtf.parse("2018-10-01");
                log.debug("{}", date);
            }).start();
        }
    }

    /**
     * 可变类例子
     * 出现异常问题
     * 解决：加锁
     */
    private static void mutable() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    log.debug("{}", sdf.parse("1951-04-21"));
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }
    }
}
