package com.khoi.service.implement;

import com.khoi.entity.HomeCategory;
import com.khoi.repository.HomeCategoryRepository;
import com.khoi.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {
    private final HomeCategoryRepository homeCategoryRepository;


    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory existCategory = homeCategoryRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Category not found"));
        if(homeCategory.getImage() != null) {
            existCategory.setImage(homeCategory.getImage());
        }

        if(homeCategory.getCategoryId() != null) {
            existCategory.setCategoryId(homeCategory.getCategoryId());
        }

        return homeCategoryRepository.save(existCategory);
    }

    @Override
    public List<HomeCategory> createCategory(List<HomeCategory> homeCategories) {
        if(homeCategoryRepository.findAll().isEmpty()) {
            return homeCategoryRepository.saveAll(homeCategories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public List<HomeCategory> getAllHomeCategory() {
        return homeCategoryRepository.findAll();
    }
}
