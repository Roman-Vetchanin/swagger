package com.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfoService {
    @Value("${server.port}")
    private int port;

    public int getPort() {
        return port;
    }
}
