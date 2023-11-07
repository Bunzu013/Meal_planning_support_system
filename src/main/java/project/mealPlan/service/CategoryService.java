package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.Category;
import project.mealPlan.repository.CategoryRepository;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public ResponseEntity<?>  getAllCategories()
    {
        try {
            List<Map<String,Object>> categoryInfoList = new ArrayList<>();
            List<Category> categories = categoryRepository.findAll();
            for(Category category : categories)
            {
                Map<String,Object> categoryInfo = new HashMap<>();
                categoryInfo.put("categoryName", category.getRecipeCategoryName());
                categoryInfoList.add(categoryInfo);
            }
            return new ResponseEntity<>(categoryInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}