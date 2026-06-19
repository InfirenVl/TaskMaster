package com.infiren.taskmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @GetMapping("/")
    public String getHomePage() {
        return "Hello World " +
                "Main page";
    }

}
