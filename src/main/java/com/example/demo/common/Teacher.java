package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Teacher {
    private String name;
    private Integer age;
    private Map<String, String> map;


}
