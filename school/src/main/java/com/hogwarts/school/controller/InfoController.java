package com.hogwarts.school.controller;

import com.hogwarts.school.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping
    public int getPort() {
        return infoService.getPort();
    }
}
