package com.khoi.service;

import com.khoi.entity.User;

public interface UserService {
    User getUserByJwtToken(String token) throws Exception;
    User getUserByEmail(String email) throws Exception;
}
