package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @AllArgsConstructor 会创建一个全参数的构造函数，但是默认的无参构造函数会没有。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student{
    private String name;
    private Integer age;

}
