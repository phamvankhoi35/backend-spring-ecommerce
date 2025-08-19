package com.khoi.service.implement;

import com.khoi.entity.Deal;
import com.khoi.entity.HomeCategory;
import com.khoi.repository.DealRepository;
import com.khoi.repository.HomeCategoryRepository;
import com.khoi.service.DealService;
import com.khoi.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeal() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository
                .findById(deal.getCategory().getId())
                .orElse(null);
        Deal newDeal = dealRepository.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());

        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        Deal existDeal = dealRepository.findById(id).orElse(null);
        HomeCategory category = homeCategoryRepository
                .findById(deal.getCategory().getId())
                .orElseThrow(() -> new Exception("Deal not found"));

        if(existDeal != null) {
            if(deal.getDiscount() != null) {
                existDeal.setDiscount(deal.getDiscount());
            }
            if(category != null) {
                existDeal.setCategory(category);
            }
            return dealRepository.save(existDeal);
        }

        throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Deal not found"));
        dealRepository.delete(deal);
    }
}
