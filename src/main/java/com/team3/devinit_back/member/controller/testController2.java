package com.team3.devinit_back.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class testController2 {
    @GetMapping("/test2")
    @ResponseBody
    public String testAPI2(){
        return "test2 route";
    }
}
