package com.khoi.service;

import com.khoi.entity.Deal;

import java.util.List;

public interface DealService {
    List<Deal> getDeal();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal, Long id) throws Exception;
    void deleteDeal(Long id) throws Exception;

}
