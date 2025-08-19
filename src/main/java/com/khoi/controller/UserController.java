package com.khoi.controller;

import com.khoi.entity.User;
import com.khoi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<User> getUserHandler(
            @RequestHeader("Authorization") String token
    ) throws Exception {

        User user= userService.getUserByJwtToken(token);

        return ResponseEntity
                .status(200)
                .body(user);
    }

}
