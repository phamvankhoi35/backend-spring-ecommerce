package com.khoi.service;

import com.khoi.entity.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception;
    List<HomeCategory> createCategory(List<HomeCategory> homeCategories);
    List<HomeCategory> getAllHomeCategory();
}
