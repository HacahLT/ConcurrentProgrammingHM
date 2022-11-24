package com.hacah.nolock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hacah
 * @date 2022/11/23 9:57
 */
public class AtomicIntegerDemo1 {


    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(0);


        int i = atomicInteger.incrementAndGet();
        System.out.println(i);


        int i1 = atomicInteger.addAndGet(2);
        System.out.println(i1);


        int i2 = atomicInteger.updateAndGet(operand -> operand + 2);
        System.out.println(i2);


        int i3 = atomicInteger.accumulateAndGet(13, (innI, param) -> innI - param);
        System.out.println(i3);


    }
}
