package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class MealService {
        @Autowired
        RecipeRepository recipeRepository;

        @Autowired
        MealRepository mealRepository;

        @Transactional
        public ResponseEntity<?> addRecipeToMeal(Map<String,Object> recipeData) {
            try {
                Integer recipeId = (Integer) recipeData.get("recipeId");
                Integer mealId = (Integer) recipeData.get("mealId");
                Meal meal = mealRepository.findByMealId(mealId);
                if (meal != null) {
                Recipe recipe = recipeRepository.findByRecipeId(recipeId);
                    if (recipe != null) {
                        if (!meal.getMealRecipes().contains(recipe)) {
                            meal.getMealRecipes().add(recipe);
                            mealRepository.save(meal);
                            return ResponseEntity.status(HttpStatus.OK).body("Recipe added to meal");
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe already exists in the meal");
                        }
                    }else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipe found");
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No meal found");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }


    }


