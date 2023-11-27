package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.Category;
import project.mealPlan.entity.Recipe;
import project.mealPlan.repository.CategoryRepository;
import project.mealPlan.repository.RecipeRepository;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RecipeRepository recipeRepository;
    public ResponseEntity<?>  getAllCategories()
    {
        try {
            List<Map<String,Object>> categoryInfoList = new ArrayList<>();
            List<Category> categories = categoryRepository.findAll();
            for(Category category : categories)
            {
                Map<String,Object> categoryInfo = new HashMap<>();
                categoryInfo.put("categoryId", category.getRecipeCategoryId());
                categoryInfo.put("categoryName", category.getRecipeCategoryName());
                categoryInfoList.add(categoryInfo);
            }
            return new ResponseEntity<>(categoryInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?>  addNewCategory(Map<String, Object> categoryInput)
    {
        try {
            String category = (String) categoryInput.get("categoryName");
            if(categoryRepository.findByRecipeCategoryName(category) == null){
                Category newCategory = new Category(category);
                categoryRepository.save(newCategory);
                return new ResponseEntity<>("New category added", HttpStatus.OK);
            }
            return new ResponseEntity<>("Category already exists", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> updateCategory(Map<String, Object> categoryInput)
    {
        try {
            Integer categoryId = (Integer) categoryInput.get("categoryId");
            String categoryName = (String) categoryInput.get("categoryName");
            if(categoryRepository.findByRecipeCategoryName(categoryName) == null) {
                if (categoryId != null) {
                    Category categoryUpdate = categoryRepository.findByRecipeCategoryId(categoryId);
                    if (categoryUpdate != null) {
                        categoryUpdate.setRecipeCategoryName(categoryName);
                        categoryRepository.save(categoryUpdate);
                        return new ResponseEntity<>("Category updated", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("No category found", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }else{
                return new ResponseEntity<>("Category with this name already exists", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>("Error updating category", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteCategory(Map<String, Object> categoryInput)
    {
        try {
            Integer categoryId = (Integer) categoryInput.get("categoryId");
                if (categoryId != null) {
                    Category category = categoryRepository.findByRecipeCategoryId(categoryId);
                    if (category != null) {
                        List<Recipe> recipes = recipeRepository.findByRecipeCategories(category);
                        for (Recipe recipe : recipes) {
                            recipe.getRecipeCategories().remove(category);
                        }
                        categoryRepository.delete(category);
                        return new ResponseEntity<>("Category deleted", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("No category found", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

            return new ResponseEntity<>("Error deleting category", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}