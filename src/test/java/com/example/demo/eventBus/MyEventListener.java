package com.example.demo.eventBus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyEventListener {

    /**
     * 监听integer类型的事件
     * @param param
     */
    @Subscribe
    public void listenerInteger(Integer param) throws InterruptedException {
        Thread.sleep(3000L);
        log.info("enter listenerInteger, thread:{}, param:{}", Thread.currentThread().getName(), param);
    }

    /**
     * 监听OrderCreateEvent类型的事件
     * @param param
     */
    @Subscribe
    public void listenerOrderCreateEvent(OrderCreateEvent param) throws InterruptedException {
        Thread.sleep(4000L);
        log.info("enter listenerOrderCreateEvent, thread:{}, param:{}", Thread.currentThread().getName(), param);
    }

    /**
     * 监听DeadEvent类型的事件: 死信事件，接受没有订阅者的消息
     * @param param
     */
    @Subscribe
    public void listenerDeadEvent(DeadEvent param) throws InterruptedException {
        Thread.sleep(2000L);
        log.info("enter listenerDeadEvent, thread:{}, param:{}", Thread.currentThread().getName(), param);
    }
}
