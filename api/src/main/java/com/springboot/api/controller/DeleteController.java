package com.springboot.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delete-api")
public class DeleteController {

    // http://localhost:8080/api/v1/delete-api/{String 값}
    @DeleteMapping(value = "/{variable}")  // value의 이름과 매개변수명이 같아야 함.
    public String DeleteVariable(@PathVariable String variable) {
        return variable;
    }

    // http://localhost:8080/api/v1/delete-api/request1?email=value
    @DeleteMapping(value = "/request1") // 쿼리 스트링 값을 받을 수 있음.
    public String getRequestParam1(@RequestParam String email) {
        return "e-mail : " + email;
    }


}
