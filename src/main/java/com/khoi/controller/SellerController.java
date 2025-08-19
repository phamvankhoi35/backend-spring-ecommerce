package com.khoi.controller;

import com.khoi.config.JWTProvider;
import com.khoi.domain.AccountStatus;
import com.khoi.dto.request.LoginRequest;
import com.khoi.dto.response.ApiResponse;
import com.khoi.dto.response.AuthResponse;
import com.khoi.entity.Seller;
import com.khoi.entity.SellerReport;
import com.khoi.entity.VerificationCode;
import com.khoi.exception.SellerException;
import com.khoi.repository.SellerReportRepository;
import com.khoi.repository.VerificationCodeRepository;
import com.khoi.service.AuthService;
import com.khoi.service.EmailService;
import com.khoi.service.SellerReportService;
import com.khoi.service.SellerService;
import com.khoi.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;

    private final EmailService emailService;
    private final JWTProvider jwtProvider;
    private final SellerReportService sellerReportService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(
            @RequestBody LoginRequest request
            ) throws Exception {
        String otp = request.getOtp();
        String email = request.getEmail();
        request.setEmail("seller_" + email);

        System.out.println(otp + " - " + email);

        AuthResponse authResponse = authService.signing(request);
        return ResponseEntity
                .status(200)
                .body(authResponse);

    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @PathVariable String otp
    ) throws Exception {
        // xác minh otp
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong OTP ...");
        }
        // xác minh email
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    // tạo mới người bán
    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception, MessagingException {

        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "";
        String text = "";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByToken(@RequestHeader("Authorization") String token) throws Exception {
        // get data seller by token
        Seller seller = sellerService.getSellerProfile(token);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(token);
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(sellerReport, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(
            @RequestParam(required = false) AccountStatus status
    ) throws Exception {
        List<Seller> sellers = sellerService.getAllSeller(status);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String token,
            @RequestBody Seller seller
    ) throws Exception {
        Seller profile = sellerService.getSellerProfile(token);
        Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);
        return new ResponseEntity<>(updateSeller, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
