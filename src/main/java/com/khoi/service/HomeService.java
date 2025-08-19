package com.khoi.service;

import com.khoi.entity.Home;
import com.khoi.entity.HomeCategory;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
