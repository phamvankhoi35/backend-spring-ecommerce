package com.khoi.controller;

import com.khoi.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:3000")
@RestController
//@RequestMapping()
public class HomeController {

    @GetMapping
    public ApiResponse HomeControllerHandler() {
        ApiResponse res = new ApiResponse();
        res.setMessage("Welcome to Summoner Rift");
        return res;
    }
}
