package com.khoi.service;

import com.khoi.entity.Order;
import com.khoi.entity.Seller;
import com.khoi.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransaction();
}
