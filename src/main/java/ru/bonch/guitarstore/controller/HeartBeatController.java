package ru.bonch.guitarstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {

    @GetMapping("/ping")
    public String pong(){
        return "pong";
    }
}
