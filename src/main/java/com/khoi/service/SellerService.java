package com.khoi.service;


import com.khoi.domain.AccountStatus;
import com.khoi.entity.Seller;
import com.khoi.exception.ProductException;
import com.khoi.exception.SellerException;

import java.util.List;

public interface SellerService {
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerProfile(String token) throws ProductException, SellerException;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;
    Seller updateSeller(Long id, Seller seller) throws Exception;
    Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception;
    List<Seller> getAllSeller(AccountStatus status); // dữ liệu đầu vào là status vì chỉ lấy những gian hàng còn hoạt động
    void deleteSeller(Long id) throws Exception;
}
