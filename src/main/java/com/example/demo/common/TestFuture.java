package com.example.demo.common;

import com.google.common.collect.Lists;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

public class TestFuture {

    private static ThreadPoolExecutor noticePool = new ThreadPoolExecutor(
            5,
            5,
            10,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(100),
            new CustomizableThreadFactory("my-pool"),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public static void main(String[] args) {

        List<CompletableFuture<String>> list = Lists.newArrayListWithCapacity(10);

        for (int i = 0; i < 50; i++) {

            CompletableFuture<String> resourceGroupResultCompletableFuture = CompletableFutureTemplate.orTimeout(CompletableFuture.supplyAsync(() -> {
                Long id = System.currentTimeMillis();
                return sayHello(id);
            }, noticePool), Duration.ofMillis(5000));
            //这里的Duration.ofMillis(5000) 是完成50个任务一共的总时间。
            list.add(resourceGroupResultCompletableFuture);
        }
        System.out.println("over 1");
        list.stream().forEach(x -> {
            try {
                String s = x.get();
                System.out.println(s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private static String sayHello(Long id) {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(0, 2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hello " + id;
    }
}
