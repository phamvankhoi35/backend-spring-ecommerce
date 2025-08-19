package com.khoi.service.implement;

import com.khoi.domain.HomeCategorySection;
import com.khoi.entity.Deal;
import com.khoi.entity.Home;
import com.khoi.entity.HomeCategory;
import com.khoi.repository.DealRepository;
import com.khoi.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategory = allCategories
                .stream()
                .filter(category -> category.getSection() == HomeCategorySection.GRID)
                .toList();

        List<HomeCategory> shopByCategory = allCategories
                .stream()
                .filter(category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
                .toList();

        List<HomeCategory> electricCategory = allCategories
                .stream()
                .filter(category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
                .toList();

        List<HomeCategory> dealCategory = allCategories
                .stream()
                .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                .toList();

        List<Deal> createdDeal = new ArrayList<>();
        if(dealRepository.findAll().isEmpty()) {
            List<Deal> deals = allCategories
                    .stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .collect(Collectors.toList());

            createdDeal = dealRepository.saveAll(deals);
        } else {
            createdDeal = dealRepository.findAll();
        }

        Home home = new Home();
        home.setGrid(gridCategory);
        home.setShopByCategories(shopByCategory);
        home.setElectricCategories(electricCategory);
        home.setDealCategories(dealCategory);
        home.setDeals(createdDeal);


        return home;
    }
}
