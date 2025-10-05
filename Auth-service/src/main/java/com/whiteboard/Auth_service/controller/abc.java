package com.whiteboard.Auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abc")
public class abc {
    @GetMapping("/de")
    public String as(){
        return "hhh";
    }
}
