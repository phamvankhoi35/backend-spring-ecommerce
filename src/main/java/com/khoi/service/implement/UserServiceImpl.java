package com.khoi.service.implement;

import com.khoi.config.JWTProvider;
import com.khoi.entity.User;
import com.khoi.repository.UserRepository;
import com.khoi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Override
    public User getUserByJwtToken(String token) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(token);

//        User user = this.findUserByEmail(email);
//        if(user == null) {
//            throw new Exception("User not found with email : " + email);
//        }

        return this.getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found with email : " + email);
        }
        return user;
    }
}
