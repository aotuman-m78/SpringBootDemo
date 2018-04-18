package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fangrui on 2018/2/7.
 */
@Controller
@RequestMapping("demo")
public class DemoController {

    @ResponseBody
    @RequestMapping("index")
    public String index() {
        return "hello world";
    }
}
