package com.khoi.service.implement;

import com.khoi.config.JWTProvider;
import com.khoi.domain.USER_ROLE;
import com.khoi.dto.request.LoginRequest;
import com.khoi.dto.response.AuthResponse;
import com.khoi.entity.Cart;
import com.khoi.entity.Seller;
import com.khoi.entity.User;
import com.khoi.entity.VerificationCode;
import com.khoi.repository.CartRepository;
import com.khoi.repository.SellerRepository;
import com.khoi.repository.UserRepository;
import com.khoi.repository.VerificationCodeRepository;
import com.khoi.dto.request.SignupRequest;
import com.khoi.service.AuthService;
import com.khoi.service.EmailService;
import com.khoi.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final SellerRepository sellerRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;
    private final EmailService emailService;

    private final CustomUserServiceImpl customUserServiceImpl;


    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {
//        String SELLER_PREFIX = "seller_";
        String SIGNING_PREFIX = "signin_";

        if(email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null) {
                    throw new Exception("Seller not found");
                }
            } else {
                System.out.println("Email la : " + email);
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new Exception("User not exist with provided email");
                }
            }

        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if(isExist != null) {
            // nếu mã otp đã tồn tại thì xoá mã otp
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "khoi gui otp";
        String text = "Ma otp la : " + otp;

        // bắt đầu gửi mã cho email
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest request) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if(verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
            throw new Exception("wrong otp ...");
        }

        User user = userRepository.findByEmail(request.getEmail());

        if(user == null) {
            User new_user = new User();
            new_user.setEmail(request.getEmail());
            new_user.setFullName(request.getFullname());
            new_user.setRole(USER_ROLE.ROLE_CUSTOMER);
            new_user.setPhone("");
            new_user.setPassword(passwordEncoder.encode(request.getOtp()));

            user = userRepository.save(new_user);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest request) throws Exception {
        String username = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Login success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
        UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String SELLER_PREFIX = "seller_";
        if(username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong OTP");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }


}
