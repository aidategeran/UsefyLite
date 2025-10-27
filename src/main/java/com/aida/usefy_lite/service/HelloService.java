package com.aida.usefy_lite.service;


import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String getGreeting() {
        return "Hello, Aida! Welcome to UsifyLite!";
    }
}
