package com.example.demo.common;

import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TestFunc {

    /**
     * 这里举例说明了Function 作为参数的用法，一般是把公用的处理逻辑抽出来，其他基于这个结果的业务逻辑都可以把自身的业务做成一个func传进到公共方法中去。
     * 例如：biz1, biz2, 分别代表用传入的参数获取的结果，需要根据自身的逻辑来处理。这里获取参数结果的过程是个公共方法，大家都能用，不同的每家的业务不一样。如何处理，func 里面自己定义处理逻辑。
     * @param args
     */
    public static void main(String[] args) {
        TestFunc testFunc = new TestFunc();
        Boolean b = testFunc.biz1();
        Integer i = testFunc.biz2();
        System.out.println(b);
        System.out.println(i);
        List<String> list = Lists.newArrayList("a","b");
        Map<String, String> leftMap = Maps.newHashMap();
        leftMap.put("a","aaa");
        leftMap.put("b","bbb");
        leftMap.put("c","ccc");
        Map<String, String> rightMap = Maps.newHashMap();
        rightMap.put("a","aaa");
        rightMap.put("b","bbb");
        rightMap.put("c","ccc");
        boolean b1 = JsonUtils.jsonDiff(JsonUtils.toJson(leftMap), JsonUtils.toJson(rightMap));
        System.out.println(b1);
    }


    public Boolean biz1() {
        Boolean b = performRequest("aCluster", "aIndex", "aType", list -> {
            if (list.contains("abc")) {
                return true;
            }
            return false;
        });
        System.out.println("the result is: " + b);
        return b;
    }

    public Integer biz2() {
        Integer size = performRequest("aCluster", "aIndex", "aType", list -> {
            if (list.contains("abc")) {
                return list.size();
            }
            return list.size() + 1;
        });
        System.out.println("the result is: " + size);
        return size;
    }

    public <R> R performRequest(String clusterName, String indexName, String typeName, Function<List<String>, R> func) {
        List<String> response = doQuery(clusterName, indexName, typeName);
        return func.apply(response);
    }

    public List<String> doQuery(String index, String query, String typeName) {
        return Lists.newArrayList(index, query, typeName);
    }
}
