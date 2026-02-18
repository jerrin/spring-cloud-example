package net.jerrin.demo.mvcservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MvcService {

    @GetMapping("/get")
    public String getMessage() {
        return "Hello from MVC Service!";
    }
}
