package com.khoi.service;

import com.khoi.entity.Seller;
import com.khoi.entity.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);

}
