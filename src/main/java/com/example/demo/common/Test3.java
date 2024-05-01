package com.example.demo.common;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

public class Test3 {

    private static ThreadPoolExecutor noticePool = new ThreadPoolExecutor(
            5,
            5,
            10,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(100),
            new CustomizableThreadFactory("my-pool"),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );
    private static Map<String, String> MAP = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
//        System.out.println("HELLO world, 你好吗？");
        //this is my test for the keymap, I will try my best to do it.sfsdff


//        List<Integer> dataList = new ArrayList<>();
//        for (int i = 1; i <= 102; i++) {
//            dataList.add(i);
//        }
//        int pageSize = 10;
//        List<List<Integer>> pageList = Lists.partition(dataList, pageSize);
//
//        for (int i = 0; i < pageList.size(); i++) {
//            System.out.println("Page " + (i + 1) + ": " + pageList.get(i));
//        }
//
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        System.out.println(atomicInteger.getAndAdd(1));
//        System.out.println(atomicInteger.getAndAdd(1));
//        System.out.println(atomicInteger.getAndAdd(1));
//        System.out.println(atomicInteger);
//        List<String> list = Lists.newArrayList("a", "b", "c", "d");
//        String s = "abcde";
//        String bucket = "d,e";
//
//        boolean b = Arrays.stream(bucket.split(",")).anyMatch(s::contains);
//        System.out.println(b);
//
//        System.out.println(Parent.class.isAssignableFrom(Base.class));
//        System.out.println(Parent.class.isAssignableFrom(Child.class));
//        System.out.println(Parent.class.isAssignableFrom(Parent.class));

//        TestBean bean = new TestBean();
//
//        Map<String, String> map = Maps.newHashMap();
//        String put = map.put("a", "b");//{"a","b"}
//        System.out.println(put);//null
//        String put1 = map.put("a", "c");//{"a","c"}
//        System.out.println(put1);//"b"
//        String a = map.compute("a", (k, v) -> "v");//{"a","v"}
//        System.out.println(a);//"v"
//        String s = map.putIfAbsent("a", "xxx");//{"a","v"} 不存在才put
//        System.out.println(s);//"v"
//        String s1 = map.computeIfAbsent("a", k -> "xxx");//{"a","v"}
//        System.out.println(s1);//v
//        String s2 = map.computeIfPresent("a", (k, v) -> "yyyy");//{"a","yyyy"}
//        System.out.println(s2);//"yyyy"
//        String ss = "abc";
//        System.out.println(ss.substring(0, ss.length() - 1));
//        System.out.println(ss);
//
//        List<Student> list = Lists.newArrayList(new Student("bllen", 9), new Student("cllen", 11), new Student("allen", 9), new Student("allen", 10), new Student("sdf", 23));
//        System.out.println(list);
////        List<Student> collect = list.stream().sorted(Comparator.comparing(Student::getAge).thenComparing(Student::getName)).collect(Collectors.toList());
//        list.sort(Comparator.comparing(Student::getAge).thenComparing(Student::getName));
//        System.out.println(list);
//
//        System.out.println(StringUtils.defaultIfEmpty("intentionCity", StringUtils.EMPTY));
//        System.out.println(StringUtils.defaultIfEmpty(null, "StringUtils.EMPTY"));
//
//        Map<String, List<Student>> bizUvRecordMap = Maps.newHashMap();
//        bizUvRecordMap.computeIfAbsent("a", k -> Lists.newArrayList()).add(new Student("allen", 10));
//        System.out.println(bizUvRecordMap);
//        Student st = new Student("a", 1);
////        Student st = null;
//        if (st == null || st.getAge() <= 0) {
//            return;
//        }
//
//        Map<String, Student> collect = list.stream().collect(Collectors.toMap(u -> u.getName() + "_" + u.getAge(), Function.identity(), (k1, k2) -> k1));
//        System.out.println(collect);
//
//        Teacher t = new Teacher("a",1, null);
//        String s3 = Optional.ofNullable(t.getMap()).map(x -> x.get("af")).orElse(null);
//        System.out.println(s3);
//
//        Stopwatch stopwatch = Stopwatch.createStarted();
//        TimeUnit.SECONDS.sleep(2);
//        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
//
        System.out.println(checkParam("", "", null));
        System.out.println(checkParam2(" a ", "sdf", "null"));
//
//        TestObj testObj = new TestObj();
//        System.out.println(testObj);
//        Map<String, List<Student>> collect1 = list.stream().collect(Collectors.groupingBy(Student::getName));
//        System.out.println(collect1);

//        CompletableFuture.runAsync(() -> System.out.println("hhheeellll"), noticePool);
//        System.out.println("over!!!");

        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "1");
        map.put("c", "3");
        map.put("d", "4");
        map.put("b", "2");
        System.out.println(map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&")));

//        String userInfoStr = StringUtils.defaultIfEmpty("", "b");
//        System.out.println(userInfoStr);
        Student student = new Student("allen", 18);
        Student student2 = new Student("bob", 19);
        Student student3 = new Student("fitz", 20);
        List<Student> list = Lists.newArrayList(student, student2, student3);
        list.stream().collect(Collectors.toList());
        Set<Student> collect = list.stream().collect(Collectors.toSet());

//        Student student = new Student();
//        student.setName("allen");
//        student.setAge(18);
//        Student student2 = new Student();
//        student2.setName("bob");
//        student2.setAge(19);
//        Student student3 = new Student();
//        student3.setName("fitz");
//        student3.setAge(20);
//        List<Student> students = Lists.newArrayList(student, student2, student3);
//        String json = JsonUtils.toJson(students);
//        System.out.println(json);
//
//        List<Student> newStudents = JsonUtils.fromJson(json, JsonUtils.buildCollectionType(List.class, Student.class));
//        System.out.println(newStudents);

        Map<String, List<String>> abc = Maps.newHashMap();
        abc.computeIfAbsent("a",k -> Lists.newArrayList()).add(System.currentTimeMillis()+"");
        abc.computeIfAbsent("b",k -> Lists.newArrayList()).add(System.currentTimeMillis()+"");
        abc.computeIfAbsent("a",k -> Lists.newArrayList()).add(System.currentTimeMillis()+"");
        abc.computeIfAbsent("a",k -> Lists.newArrayList()).add(System.currentTimeMillis()+"");
        System.out.println(abc);
        System.out.println(StringUtils.isBlank("  "));


    }



    private static boolean checkParam(String... params) {
        for (String param : params) {
            if (StringUtils.isEmpty(param)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkParam2(String... params) {
        return !Arrays.stream(params).anyMatch(StringUtils::isEmpty);
    }

}
