package com.example.demo.common;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Triple;

public class Test {
    private static final ScheduledThreadPoolExecutor DELAYER;
    private static final LoadingCache<Integer, String> cache;

    static {
        (DELAYER = new ScheduledThreadPoolExecutor(1,  new CustomizableThreadFactory("test-pool"))).setRemoveOnCancelPolicy(true);
        GuavaCacheService guavaCacheService = new GuavaCacheService();
        cache = guavaCacheService.createCache();
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {

//        Student student = new Student("name", 18);
//        StudentVO studentVO = StudentMapper.INSTANCE.studentToStudentVO(student);
//        System.out.println(studentVO);
//
//
//
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(now);
//        // 获取周五时间18点整的时间
//        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(10).withNano(0).with(DayOfWeek.FRIDAY);
//        System.out.println(time);


//        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(".........doing..........");
//            return "hello world";
//        });
//        System.out.println("xxxxxxxxx");
//        CompletableFuture<String> stringCompletableFuture = orTimeout(cf, Duration.ofSeconds(1));
//
//        stringCompletableFuture.whenComplete((s, throwable) -> {
//            System.out.println(s);
//            if (throwable != null) {
//                throwable.printStackTrace();
//            }
//        });

//        String name = "allen";
//        String name = null;
//        CompletableFuture<String> completableFuture =  CompletableFuture.supplyAsync(() -> {
//            if (name == null) {
//                throw new RuntimeException("Computation error!");
//            }
//            return "Hello, " + name;
//        }).handle((s, t) -> s != null ? s : "Hello, Stranger!");

//        doTest();
//        System.out.println("over");

//        allOf2();

//        test1();

//        System.out.println(StringUtils.isBlank("sdf"));
//        System.out.println(StringUtils.isBlank(""));
//        System.out.println(StringUtils.isBlank(" "));
//        System.out.println(StringUtils.isBlank("  "));
//        System.out.println(StringUtils.isBlank(null));
//
//        List<String> ss = Lists.newArrayList("a", "b", "c", "d", "e", "f");
//
//
//        String low = StringUtils.join(ss, "||");
//        System.out.println(low);
//
//        Pair<String, String> pair = Pair.of("one", "two");
//        System.out.println(pair);
//        System.out.println(pair.getLeft());
//        System.out.println(pair.getRight());
//        System.out.println(pair.getKey());
//        System.out.println(pair.getValue());
//
//
//        Triple<String, String, String> triple = Triple.of("a", "b", "c");
//        System.out.println(triple.getLeft());
//        System.out.println(triple.getMiddle());
//        System.out.println(triple.getRight());
        doTest2();

    }

    /**
     * 简单的例子
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581182447650
     * @throws InterruptedException
     */
    public static void test1() throws InterruptedException {
        // 第一个任务:
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油");
        });
        // cfQuery成功后继续执行下一个任务:
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice(code);
        });
        // cfFetch成功后打印结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(2000);
    }

    static String queryCode(String name) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    static Double fetchPrice(String code) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }

    public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, Duration duration) {
        CompletableFuture<T> timeout = timeout(duration);
        return future.applyToEither(timeout, Function.identity()).whenComplete((t, throwable) -> {
            if (throwable != null) {
                future.cancel(true);
            }

        });
    }

    private static <T> CompletableFuture<T> timeout(Duration duration) {
        CompletableFuture<T> promise = new CompletableFuture();
        DELAYER.schedule(() -> {
            TimeoutException ex = new TimeoutException("Timeout after " + duration);
            return promise.completeExceptionally(ex);
        }, duration.toMillis(), TimeUnit.MILLISECONDS);
        return promise;
    }

    private static void doTest() {
        ExecutorService es = Executors.newFixedThreadPool(4);
        List<Runnable> tasks = getTasks(5);
        CompletableFuture<?>[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, es))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        es.shutdown();
    }

    /**
     * 模拟了多个CF并发执行，每个任务都会调用guava缓存，看看具体如何执行。
     * 同一个key被调用时，如果缓存不存在，其他线程等待。直到有值为止。然后等缓存到期侯，第一个命中的线程会去重新拉取新的数据，其他线程继续获取老的值。
     */
    private static void doTest2() {
        ExecutorService es = Executors.newFixedThreadPool(4);
        List<String> list = Lists.newArrayList("a", "b", "c", "d","e", "f","g", "h","i","j", "k", "l", "m");
        List<CompletableFuture<String>> collect = list.stream()
                .map(param -> CompletableFuture.supplyAsync(() -> doSomeThing(param), es))
                .collect(Collectors.toList());
        System.out.println("================================================================");
        List<String> collect1 = collect.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
        collect1.forEach(System.out::println);

    }

    private static List<Runnable> getTasks(int n) {
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final int j = i;
            tasks.add(() -> {
                System.out.println(Thread.currentThread().getName() + "--->" + j);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return tasks;
    }

    private static String doSomeThing(String s) {
        try {
            String s1 = "";
            for (int i = 0; i < 30; i++) {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000));
                s1 = cache.get(1);
                System.out.println(Thread.currentThread().getName() + "loading ->" + s1);
            }
            return Thread.currentThread().getName() + " ending with ->" + s + "->" + s1;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public static void allOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("hello");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("beautiful");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(4);
                System.out.println("world");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.allOf(future1, future2, future3).join();
        System.out.println("oooooxxxxxxxxx");
    }

    public static void allOf2(){
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("hello");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Hello";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Beautiful");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Beautiful";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(4);
                System.out.println("World");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "World";
        });

        String combined = Stream.of(future1, future2, future3)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));
        System.out.println(combined);
    }
}
