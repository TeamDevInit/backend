package com.team3.devinit_back.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testController1 {
    @GetMapping("/")
    @ResponseBody
    public String testAPI1(){
        return "test1 route";
    }
}
