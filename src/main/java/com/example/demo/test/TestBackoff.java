package com.example.demo.test;

import com.example.demo.backoff.ExponentialBackOff;
import com.example.demo.backoff.Retry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestBackoff {
    public static void main(String[] args) {
        retry7();
    }

    /**
     * 持续获取某个int值，直到获取大于80的值，按照Exponential时间间隔递增的方式，最大时间间隔不能超过60秒(DEFAULT_MAX_ELAPSED_TIME), 总共耗时不要超过5分钟(DEFAULT_MAX_ELAPSED_TIME),执行结果如下。
     *
     * main: 20
     * 18:13:48.630 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT1S
     * main: 14
     * 18:13:49.743 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT1.5S
     * main: 39
     * 18:13:51.288 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT2.25S
     * main: 69
     * 18:13:53.618 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT3.375S
     * main: 50
     * 18:13:57.093 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT5.062S
     * main: 39
     * 18:14:02.247 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT7.593S
     * main: 42
     * 18:14:09.897 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT11.389S
     * main: 58
     * 18:14:21.326 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT17.083S
     * main: 36
     * 18:14:38.487 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT25.624S
     * main: 31
     * 18:15:04.184 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT38.436S
     * main: 34
     * 18:15:42.657 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT57.654S
     * main: 51
     * 18:16:40.386 [main] WARN com.example.demo.backoff.Retry - Run into error, will retry after PT1M
     * main: 87
     */

    public static void retry7() {
        Retry.simpleExponential(5, 2, TimeUnit.SECONDS, () -> sayHello());
    }

    public static void retry6() {
        Retry.simple(3, 2, TimeUnit.SECONDS, () -> sayHello());
    }

    public static void retry5() {
        Integer simple = Retry.simple(3, 2, TimeUnit.SECONDS, () -> getValue());
        System.out.println(simple);
    }

    public static void retry4() {
        Retry.withTimes(ExponentialBackOff.getDefault(), TimeUnit.MILLISECONDS, () -> sayHello(), 3);
    }

    public static void retry3() {
        Retry.withTimes(ExponentialBackOff.getDefault(), TimeUnit.MILLISECONDS, () -> getValue(), 3);
    }

    public static void retry2() {
        Retry.withTimesAndConditions(ExponentialBackOff.getDefault(), TimeUnit.MILLISECONDS, () -> getValue(), v -> v > 80, 3);
    }

    public static int getValue() {
        int i = ThreadLocalRandom.current().nextInt(100);
        System.out.println(Thread.currentThread().getName() + ": " + i);
        if (i < 80) {
            i = i/0;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    public static void sayHello() {
        System.out.println(Thread.currentThread().getName() + ": begin");
        int i = ThreadLocalRandom.current().nextInt(100);
        System.out.println("got " + i);
        if (i < 80) {
            throw new RuntimeException("not enough");
        }
        System.out.println(Thread.currentThread().getName() + ": end");
    }

    public static void retry1() {
        AtomicInteger i = new AtomicInteger();
        Retry.withTimesAndConditions(ExponentialBackOff.getDefault(), TimeUnit.MILLISECONDS, () -> {
            int andIncrement = i.getAndIncrement();
            System.out.println(andIncrement);
            //设置到20就会出错了
            if (i.get() < 5) {
                throw new RuntimeException("retry error");
            }
        });
    }
}
