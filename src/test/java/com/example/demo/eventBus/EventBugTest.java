package com.example.demo.eventBus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * guava eventBus:
 * 功能：类似MQ，发送事件并监听，完成业务逻辑结偶。
 * 使用步骤：
 * 1.引入guava
 * 2.new EventBus() or new AsyncEventBus()
 * 3.编写事件处理类，MyEventListener
 * 4.eventBus.register(new MyEventListener())
 * 5.在需要的地方直接eventBus.post(事件);即可。
 */
public class EventBugTest {

    /**
     * main线程，串行执行
     */
    @Test
    public void test1() {
        System.out.println("thread:" + Thread.currentThread().getName());
        EventBus eventBus = new EventBus();
        eventBus.register(new MyEventListener());

        eventBus.post(1);
        eventBus.post(1);

        OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
        orderCreateEvent.setId(1);
        eventBus.post(orderCreateEvent);

        eventBus.post("where are u?");
    }

    /**
     * AsyncEventBus 并行执行。在线程池中执行。
     */
    @Test
    public void test2() throws IOException {
        EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
//        EventBus eventBus = new EventBus();
        eventBus.register(new MyEventListener());

        eventBus.post(1);
        eventBus.post(1);

        OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
        orderCreateEvent.setId(1);
        eventBus.post(orderCreateEvent);

        eventBus.post("where are u?");
        System.in.read();

    }
}
