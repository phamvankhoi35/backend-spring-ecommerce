package com.khoi.service.implement;

import com.khoi.config.JWTProvider;
import com.khoi.domain.AccountStatus;
import com.khoi.domain.USER_ROLE;
import com.khoi.entity.Address;
import com.khoi.entity.Seller;
import com.khoi.exception.ProductException;
import com.khoi.exception.SellerException;
import com.khoi.repository.AddressRepository;
import com.khoi.repository.SellerRepository;
import com.khoi.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServicerImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String token) throws ProductException, SellerException {
        String email = jwtProvider.getEmailFromJwtToken(token);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());

        if(sellerExist != null) {
            throw new Exception("Seller already exist ... Please use different email!");
        }
        Address saveAddress = addressRepository.save(seller.getPickupAdress());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAdress(saveAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setPhone(seller.getPhone());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinesDetails(seller.getBusinesDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository
                .findById(id)
                .orElseThrow(() -> new SellerException("Seller ID {" + id + "} not found"));
    }

    @Override
    public Seller getSellerByEmail(String email) throws SellerException {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller == null) {
            throw new SellerException("Seller not found ...");
        }

        return seller;
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVefified(true);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller currentSeller = this.getSellerById(id);

        if (seller.getSellerName() != null) { currentSeller.setSellerName(seller.getSellerName()); }
        if (seller.getPhone() != null) { currentSeller.setPhone(seller.getPhone()); }
        if (seller.getEmail() != null) { currentSeller.setEmail(seller.getEmail()); }

        if (seller.getBusinesDetails() != null && seller.getBusinesDetails().getBusinessName() != null) {
            currentSeller.getBusinesDetails()
                    .setBusinessName(seller.getBusinesDetails().getBusinessName());
        }
        if (
            seller.getBankDetails() != null
            && seller.getBankDetails().getAccountHolderName() != null
            && seller.getBankDetails().getIfscCode() != null
            && seller.getBankDetails().getAccountNumber() != null
        ) {
            currentSeller.getBankDetails()
                    .setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            currentSeller.getBankDetails()
                    .setAccountNumber(seller.getBankDetails().getAccountNumber());
            currentSeller.getBankDetails()
                    .setIfscCode(seller.getBankDetails().getIfscCode());

        }

        if (
                seller.getPickupAdress() != null
                && seller.getPickupAdress().getAddress() != null
                && seller.getPickupAdress().getPhone() != null
                && seller.getPickupAdress().getCity() != null
                && seller.getPickupAdress().getState() != null
        ) {
            currentSeller.getPickupAdress().setAddress(seller.getPickupAdress().getAddress());
            currentSeller.getPickupAdress().setCity(seller.getPickupAdress().getCity());
            currentSeller.getPickupAdress().setState(seller.getPickupAdress().getState());
            currentSeller.getPickupAdress().setPhone(seller.getPickupAdress().getPhone());
            currentSeller.getPickupAdress().setPincode(seller.getPickupAdress().getPincode());
        }

        if (seller.getGSTIN() != null) { currentSeller.setGSTIN(seller.getGSTIN()); }

        return sellerRepository.save(currentSeller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);

        seller.setAccountStatus(status);

        return sellerRepository.save(seller);
    }

    @Override
    public List<Seller> getAllSeller(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        sellerRepository.delete(getSellerById(id));
    }
}
