package com.example.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by fangrui on 2018/6/5.
 */
@Data
public class Children implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer age;
}
