package com.example.demo.backoff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.example.demo.backoff.ExponentialBackOff.*;
import static com.google.common.base.Preconditions.checkNotNull;

public class Retry {

    private static final Logger LOGGER = LoggerFactory.getLogger(Retry.class);
    private static final int MAX_RETRY = Integer.MAX_VALUE;

    @Nonnull
    public static <T> T untilNonNull(@Nonnull final BackOff backOff, @Nonnull final TimeUnit backOffTimeUnit,
            @Nonnull final Callable<T> callable) {
        return withTimesAndConditions(backOff, backOffTimeUnit, callable, Objects::nonNull, MAX_RETRY);
    }

    public static void simple(@Nonnull final int times, @Nonnull final long interval, @Nonnull final TimeUnit timeUnit, @Nonnull final Runnable runnable) {
        checkNotNull(runnable, "Missing runnable!");
        simple(times, interval, timeUnit,() -> {
            runnable.run();
            return null;
        });
    }

    public static <V> V simple(@Nonnull final int times, @Nonnull final long interval, @Nonnull final TimeUnit timeUnit, @Nonnull final Callable<V> callable) {
        checkNotNull(callable, "Missing callable!");
        Long maxElapsedTime = Math.min(interval * times + 1, FixedBackOff.DEFAULT_MAX_ELAPSED_TIME);
        return withTimesAndConditions(new FixedBackOff(interval, maxElapsedTime), timeUnit, callable, ignored -> true, times);
    }

    public static <V> V simpleExponential(@Nonnull final int times, @Nonnull final long interval, @Nonnull final TimeUnit timeUnit, @Nonnull final Runnable runnable) {
        checkNotNull(runnable, "Missing runnable!");
        return simpleExponential(times, interval, timeUnit, () -> {
            runnable.run();
            return null;
        });
    }

    public static <V> V simpleExponential(@Nonnull final int times, @Nonnull final long interval, @Nonnull final TimeUnit timeUnit, @Nonnull final Callable<V> callable) {
        checkNotNull(callable, "Missing callable!");
        ExponentialBackOff exponentialBackOff = new ExponentialBackOff(interval, DEFAULT_MULTIPLIER, DEFAULT_MAX_INTERVAL, DEFAULT_MAX_ELAPSED_TIME, DEFAULT_ENABLE_JITTER);
        return withTimesAndConditions(exponentialBackOff, timeUnit, callable, ignored -> true, times);
    }




    public static <V> V withTimes(@Nonnull final BackOff backOff, @Nonnull final TimeUnit backOffTimeUnit, @Nonnull final Callable<V> callable, int n) {
        checkNotNull(callable, "Missing callable!");
        return withTimesAndConditions(backOff, backOffTimeUnit, callable, ignored -> true, n);
    }


    public static void withTimes(@Nonnull final BackOff backOff, @Nonnull final TimeUnit backOffTimeUnit, @Nonnull final Runnable runnable, int n) {
        checkNotNull(runnable, "Missing runnable!");
        withTimesAndConditions(backOff, backOffTimeUnit, () -> {
            runnable.run();
            return null;
        }, ignored -> true, n);
    }

    public static void withTimesAndConditions(@Nonnull final BackOff backOff, @Nonnull final TimeUnit backOffTimeUnit, @Nonnull final Runnable runnable) {
        checkNotNull(runnable, "Missing runnable!");
        withTimesAndConditions(backOff, backOffTimeUnit, () -> {
            runnable.run();
            return null;
        }, ignored -> true, MAX_RETRY);
    }

    public static <V> V withTimesAndConditions(@Nonnull final BackOff backOff, @Nonnull final TimeUnit backOffTimeUnit, @Nonnull final Callable<V> callable,
                                               @Nonnull final Predicate<V> predicate, int n) {
        checkNotNull(backOff, "Missing backoff!");
        checkNotNull(backOffTimeUnit, "Missing backOffTimeUnit!");
        checkNotNull(callable, "Missing callable!");
        n = n > MAX_RETRY ? MAX_RETRY : n;
        final BackOffHandler backOffHandler = backOff.start();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final V v = callable.call();
                    if (predicate.test(v)) {
                        return v;
                    }
                    keepRetrying(backOff, backOffTimeUnit, backOffHandler, null, --n);
                } catch (Exception e) {
                    keepRetrying(backOff, backOffTimeUnit, backOffHandler, e, --n);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RetryException("Retry failed eventually because current thread had been interrupted.", e);
        }
        throw new RetryException("Retry failed eventually because current thread had been interrupted.");
    }

    private static void keepRetrying(
            @Nonnull final BackOff backOff, @Nonnull final TimeUnit backOffTimeUnit, @Nonnull final BackOffHandler backOffHandler, @Nullable final Exception e, @Nullable int n
    ) throws InterruptedException {
        long nextBackOff = backOffHandler.nextBackOff();
        if (BackOffHandler.STOP == nextBackOff) {
            final long maxElapsedTime = backOff.getMaxElapsedTime();
            final String msg = String.format("Retry failed eventually after reached maximum elapsed time %s.",
                    Duration.of(maxElapsedTime, toChronoUnit(backOffTimeUnit)));
            throw new RetryException(msg, e);
        }
        if (n <= 0) {
            throw new RetryException("Retry failed eventually after times retry end", e);
        }
        LOGGER.warn("Run into error, will retry after {} and {} times left", Duration.of(nextBackOff, toChronoUnit(backOffTimeUnit)), n, e);
        backOffTimeUnit.sleep(nextBackOff);
    }

    public static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
            case NANOSECONDS:  return ChronoUnit.NANOS;
            case MICROSECONDS: return ChronoUnit.MICROS;
            case MILLISECONDS: return ChronoUnit.MILLIS;
            case SECONDS:      return ChronoUnit.SECONDS;
            case MINUTES:      return ChronoUnit.MINUTES;
            case HOURS:        return ChronoUnit.HOURS;
            case DAYS:         return ChronoUnit.DAYS;
            default: throw new AssertionError();
        }
    }

}
