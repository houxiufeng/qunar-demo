package com.example.demo.test;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<Resource> resources = new ArrayList();
        resources.add(new Resource(Lists.newArrayList("a", "b", "c"), "1"));
        resources.add(new Resource(Lists.newArrayList("d", "e", "f"), "2"));
        resources.add(new Resource(Lists.newArrayList("g", "h", "i"), "3"));
        System.out.println(resources);
        System.out.println(resources.stream().map(x -> x.getList()).collect(Collectors.toList()));
        System.out.println(resources.stream().flatMap(x -> x.getList().stream()).collect(Collectors.toList()));

//        boolean urlValid = isUrlValid("http://www.220910.xyz:8888/api/user?id=9");
        boolean urlValid = isGood("http://www.220910.xyz:8888/api/userx?id=9");
        System.out.println(urlValid);

        String s = "HOTEL,FLIGHT";
        List<String> strings = Splitter.on(",").splitToList(s);
        System.out.println(strings);


        Lists.newArrayList("a", "b", "c").forEach(x -> {
            if (x.equals("b")) {
                return;
            }
            System.out.println(x);
        });
    }

    public static boolean isUrlValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection)url.openConnection();
            //也可以通过判断code码判断是否有效
            //一般是返回200 但是不保证有些网站请求成功返回的不是200
            int responseCode = connt.getResponseCode();
            if(200 == responseCode){
                //链接有效
            }else{
                //链接无效
            }
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isGood(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");

            int responseCode = huc.getResponseCode();
            huc.disconnect();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
