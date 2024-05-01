package com.example.demo.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Resource {
    private List<String> list;
    private String name;
}
