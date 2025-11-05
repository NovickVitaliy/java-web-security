package org.example.crudapp.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class HelloAdminController {

    @GetMapping("/hello/admin")
    public String sayHelloToAdmin() {
        return "Hello Admin or Superadmin!";
    }
}