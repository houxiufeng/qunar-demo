package com.example.demo.controller;

import com.example.demo.common.CardTypeEnum;
import com.example.demo.common.JsonUtils;
import com.example.demo.common.RuleDto;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.service.GobrsService;
import com.gobrs.async.core.config.ConfigFactory;
import com.gobrs.async.core.config.GobrsAsyncRule;
import com.gobrs.async.core.holder.BeanHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GobrsService gobrsService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final ScheduledThreadPoolExecutor DELAYER;

    static {
        (DELAYER = new ScheduledThreadPoolExecutor(1,  new CustomizableThreadFactory("test-pool"))).setRemoveOnCancelPolicy(true);
    }

    @RequestMapping("/user/{id}")
    public String test(@PathVariable Long id) {
        User user = userDao.selectByPrimaryKey(id);
        System.out.println(user);
        System.out.println("===========");

//        Example example = new Example(Country.class);
//        example.setForUpdate(true);
//        example.createCriteria().andGreaterThan("id", 100).andLessThan("id",151);
//        example.or().andLessThan("id", 41);
//        List<Country> countries = mapper.selectByExample(example);

        Example example = new Example(User.class);
        example.setDistinct(true);
        example.createCriteria().andEqualTo("name", "bingo");
        User user1 = userDao.selectOneByExample(example);
        System.out.println("user1=" + user1);

        User user2 = userDao.selectByName("xxx");
        System.out.println("user2=" + user2);

        Example example2 = new Example(User.class);
        //注意：这里是属性名不是列名
        example2.createCriteria()
                .andEqualTo("name", "bingo2")
                .andGreaterThan("createOn", "2024-02-07 18:01:04")
                .andLessThan("createOn", "2024-02-07 18:18:04");
        List<User> users = userDao.selectByExample(example2);
        users.stream().forEach(System.out::println);
        return "ok";
    }

    @RequestMapping(value = "/t2")
    public String test2() {
        CompletableFuture<String> promise = new CompletableFuture();
        DELAYER.schedule(() -> {
            TimeoutException ex = new TimeoutException("Timeout after " + 2000);
            System.out.println("hhhhxxxxx");
            return promise.completeExceptionally(ex);
        }, 1000, TimeUnit.MILLISECONDS);
        return "ok";
    }

    @RequestMapping(value = "/rule")
    public String getRule(String ruleName) {
        gobrsService.gobrsTest(ruleName);
        return "ok done";
    }

    @PostMapping(value = "/rule")
    public String addRule(@RequestBody RuleDto ruleDto) {
        gobrsService.addRule(ruleDto);
        return "ok launch!";
    }

    @DeleteMapping(value = "/rule")
    public String deleteRule(String ruleName) {
        gobrsService.deleteRule(ruleName);
        return "ok done";
    }

    @GetMapping("/rules")
    @ResponseBody
    public String getRules() {
        ConfigFactory configFactory = BeanHolder.getBean(ConfigFactory.class);
        Map<String, GobrsAsyncRule> processRules = configFactory.getProcessRules();
        String json = JsonUtils.toJson(processRules);
        System.out.println(json);
        return json;
    }

    @GetMapping("/redis/{key}")
    @ResponseBody
    public Object redis(@PathVariable String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return o;
    }

    @GetMapping("/redis/{key}/{value}")
    @ResponseBody
    public Object setRedis(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value);
        return "ok";
    }

    @GetMapping("/enums")
    @ResponseBody
    public Object enums() {
        return Arrays.stream(CardTypeEnum.values()).collect(Collectors.toList());
    }
}
