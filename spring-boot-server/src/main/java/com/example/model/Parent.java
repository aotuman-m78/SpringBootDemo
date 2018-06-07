package com.example.model;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by fangrui on 2018/6/5.
 */
@Data
public class Parent {

    private String name;

    private Integer age;

    private ArrayList<Children> children = new ArrayList<>();
}
