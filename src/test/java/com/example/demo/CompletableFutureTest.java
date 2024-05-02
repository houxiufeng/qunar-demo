package com.example.demo;

import io.netty.util.concurrent.CompleteFuture;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * 使用CompletableFuture 的时候一般需要自定义线程池，不推荐使用ForkJoinPool
 */
public class CompletableFutureTest {
    private static ThreadPoolExecutor myPool = new ThreadPoolExecutor(
            5,
            5,
            1,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(100),
            new CustomizableThreadFactory("my-pool"),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * runSync: 异步执行任务且没有返回值
     */
    @Test
    public void testRunSync() {
        //这里用公共线程池ForkJoinPool
//        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(2000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("我是在线程：" + Thread.currentThread().getName() + "里执行的");
//        });

        //这里用my-pool1
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是在线程：" + Thread.currentThread().getName() + "里执行的");
        }, myPool);

        System.out.println("判断任务是否执行完毕1：" + cf.isDone());
        //阻塞等待cf执行完成，并获取异步任务的执行结果
        System.out.println("cf.join()=" + cf.join());
        /**
         * join, get都是阻塞获等待cf执行结果，区别：
         * 1.join 不抛出异常。
         * 2.get 抛出异常，且可指定超时时间。
         */
        System.out.println("判断任务是否执行完毕2：" + cf.isDone());
    }

    /**
     * supplyAsync: 与runAsync的唯一不同在于可以获取异步任务的返回值。
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testSupplyAsync() throws ExecutionException, InterruptedException {
//        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
//            System.out.println("I work in " + Thread.currentThread().getName());
//            return "supplyAsync go go go";
//        });

        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println("I work in " + Thread.currentThread().getName());
            return "supplyAsync go go go";
        }, myPool);
        System.out.println("cf.get()=" + cf.get());
    }

    /**
     * completedFuture:返回一个结果为给定值的且已经执行完成的CompletedFuture
     */
    @Test
    public void testCompletedFuture() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("fitz test");
        cf.thenAccept(result -> {
            //main 线程
            System.out.println("threadName:" + Thread.currentThread().getName() + ", result:" + result);
        });

        //通过new的方式创建
        CompletableFuture<String> future2 = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(1000L);
            future2.complete("fitz test2");
            return null;
        });
        System.out.println("future2.join() = " + future2.join());
    }

    /**
     * 如果你不想从你的回调函数中返回任何东西，只是想在future完成后执行一些代码，
     * 可以使用thenAccept or thenRun, 区别:
     * thenAccept: 作用同thenApply, 入参: 同thenApply, 返回值: 无
     * thenRun:作用同thenApply, 入参:无，返回值: 无
     */
    @Test
    public void testThenAcceptOrThenRun() {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "fitz home");
        CompletableFuture<Void> cf2 = cf.thenAccept(result -> System.out.println("thenAccept接受到上一步的值:" + result + "作为入参"))
                .thenRun(() -> System.out.println("不能使用上一步的泛型结果，也没有返回值，一般用于最后的处理"));
        System.out.println("cf2.jon()=" + cf2.join());
    }

    /**
     * thenApply: 用于定义future完成后要执行的逻辑
     * 入参：上一步执行的结果，返回值: CompletedFuture
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture
                .supplyAsync(() -> "fitz home", myPool)
                .thenApply(result -> "hello " + result)
                .thenApply(result -> result + ", it's over!");
        System.out.println(cf.get());
    }

    /**
     * thenCompose: 用于两个CompletableFuture的组合，并且他们是依赖关系，即第二个执行是在第一个完成之后
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenCompose() throws ExecutionException, InterruptedException {
        Long itemId = 2000L;
        Long userId = 3L;
        //第一种方法：使用thenApply，需要自己拆分没有组装过的结果
        CompletableFuture<CompletableFuture<OrderDTO>> cf2 = getItemById(itemId).thenApply(itemDTO -> {
            CompletableFuture<OrderDTO> order = createOrder(userId, itemDTO);
            return order;
        });
        System.out.println("创建订单1:" + cf2.get().get());

        //第二种方法：使用thenCompose更方便, 因为已经完成了组装，泛型是最终的泛型。
        CompletableFuture<OrderDTO> cf = getItemById(itemId).thenCompose(itemDTO -> {
            CompletableFuture<OrderDTO> order = createOrder(userId, itemDTO);
            return order;
        });
        System.out.println("创建订单2:" + cf.get());

    }

    /**
     * thenCombine: 组合两个没有依赖关系的future(没有依赖关系，分别是不同的线程来异步并行执行)
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> heightCF = getHeight();
        CompletableFuture<Double> weightCF = getWeight();
        //thenCombine回调是在两个future都完成之后才执行
        CompletableFuture<Double> resultCF = weightCF.thenCombine(heightCF, (w,h) -> {
            //计算bmi公式
            double heightInMeter = h / 100;
            return w / (heightInMeter * heightInMeter);
        });
        System.out.println("BMI = " + resultCF.get());
    }

    /**
     * exceptionally: 当CompletableFuture任何一步出错时，会进入此方法。
     */
    @Test
    public void testExceptionally() {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> "1111")
                .thenApply(ret -> {
                    System.out.println("thenApply 处理1");
                    return 1;
                })
                .thenApply(ret -> 2 / 0)
                .thenApply(ret -> {
                    System.out.println("thenApply 处理3");
                    return 3;
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    System.out.println("error here");
                    //任何一个分之出异常都会跑到此处，可以在此处返回一个默认值
                    return -1;
                });
        System.out.println("cf.join()=" + cf.join());
    }

    /**
     * handle 的处理逻辑像 try catch finally 中的finally， 即无论是否有异常发生它都会被调用.
     */
    @Test
    public void testHandle() {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> "1111")
                .thenApply(ret -> {
                    System.out.println("thenApply process 1");
                    return 1;
                })
                .thenApply(ret -> 2 / 0)
                .thenApply(ret -> {
                    System.out.println("thenApply process 3");
                    return 3;
                })
                .handle((ret, e) -> {
                    System.out.println("if no error happened still come to here");
                    if (e != null) {
                        e.printStackTrace();
                        return -1;
                    }
                    return ret;
                });
        System.out.println("cf.join()=" + cf.join());
    }

    //allOf:等待cfList中的所有cf执行完毕
    //CompletableFuture.allOf(cfList.toArray(new CompletableFuture[0])).join();

    //嵌套调用异步任务，不要使用一个线程池。

    //anyOf:当任意一个CompletableFuture完成的时候，返回一个新的CompletableFuture
    @Test
    public void testAnyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task1 result";
        }, myPool);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task2 result";
        }, myPool);
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task3 result";
        }, myPool);

        CompletableFuture<Object> anyOfCF = CompletableFuture.anyOf(cf1, cf2, cf3);
        System.out.println(anyOfCF.get());
    }

    private CompletableFuture<Double> getWeight() {
        return CompletableFuture.supplyAsync(() -> 80.0);
    }

    private CompletableFuture<Double> getHeight() {
        return CompletableFuture.supplyAsync(() -> 180d);
    }

    private CompletableFuture<OrderDTO> createOrder(Long userId, ItemDTO itemDTO) {
        CompletableFuture<OrderDTO> cf = CompletableFuture.supplyAsync(() -> {
            OrderDTO orderDTO = new OrderDTO(userId, itemDTO.getItemName());
            return orderDTO;
        });
        return cf;
    }

    private CompletableFuture<ItemDTO> getItemById(Long itemId) {
        CompletableFuture<ItemDTO> cf = CompletableFuture.supplyAsync(() -> new ItemDTO(itemId, "benz"), myPool);
        return cf;
    }

    @Data
    private static class OrderDTO {
        Long userId;
        String itemName;
        private OrderDTO(Long userId, String itemName) {
            this.userId = userId;
            this.itemName = itemName;
        }
    }

    @Data
    private static class ItemDTO {
        Long itemId;
        String itemName;
        private ItemDTO(Long itemId, String itemName) {
            this.itemId = itemId;
            this.itemName = itemName;
        }
    }
}
