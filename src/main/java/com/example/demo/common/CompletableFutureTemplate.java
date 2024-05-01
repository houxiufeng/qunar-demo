package com.example.demo.common;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * CompletableFuture 超时支持
 * 参考jdk9 orTimeout 实现
 */
public final class CompletableFutureTemplate {
    private CompletableFutureTemplate() {
    }

    /**
     * 如果在给定超时之前未完成，则异常完成此 CompletableFuture 并抛出 {@link TimeoutException} 。
     * *
     * * @param duration 在出现 TimeoutException 异常完成之前等待多长时间，
     * * @return 入参的 CompletableFuture
     */
    public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, Duration duration) {
        final CompletableFuture<T> timeout = timeout(duration);
        return future.applyToEither(timeout, Function.identity()).whenComplete((t, throwable) -> {
            if (throwable != null) {
                future.cancel(true);
            }
        });
    }

    /**
     * 超时任务
     */
    private static <T> CompletableFuture<T> timeout(Duration duration) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        DELAYER.schedule(() -> {
            final TimeoutException ex = new TimeoutException("Timeout after " + duration);
            return promise.completeExceptionally(ex);
        }, duration.toMillis(), MILLISECONDS);
        return promise;
    }

    /**
     * Scheduled 线程池
     */
    private static final ScheduledThreadPoolExecutor DELAYER;

    static {
        (DELAYER = new ScheduledThreadPoolExecutor(
                1, new DaemonThreadFactory())).
                setRemoveOnCancelPolicy(true);
    }

    static final class DaemonThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("CompletableFutureDelayScheduler");
            return t;
        }
    }
}

