package com.khoi.controller;

import com.khoi.entity.Seller;
import com.khoi.entity.Transaction;
import com.khoi.exception.SellerException;
import com.khoi.service.SellerService;
import com.khoi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final SellerService sellerService;

    @GetMapping("seller")
    public ResponseEntity<List<Transaction>> getTransactionBySellerId(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        Seller seller = sellerService.getSellerProfile(token);

        List<Transaction> transactions = transactionService.getTransactionBySellerId(seller);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return ResponseEntity.ok(transactions);
    }
}
