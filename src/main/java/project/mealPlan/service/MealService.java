package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class MealService {
        @Autowired
        RecipeRepository recipeRepository;

        @Autowired
        MealRepository mealRepository;
        @Autowired
        WeekDayRepository weekDayRepository;
        @Autowired
        UserRepository userRepository;
        @Autowired
        MealPlan_MealRepository  mealPlanMealRepository;
        @Autowired
        UserService userService;

    @Transactional
        public ResponseEntity<?> addRecipeToMeal(Map<String,Object> recipeData,Authentication authentication) {
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
                            return ResponseEntity.status(HttpStatus.OK)
                                    .body("Recipe added to meal");
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Recipe already exists in the meal");
                        }
                    }else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No recipe found");
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No meal found");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred");
            }
        }
@Transactional
    public ResponseEntity<?> deleteMeal(Integer mealId) {
        try {
            Meal meal = mealRepository.findByMealId(mealId);
            if (meal != null) {
                List<MealPlan_Meal> mealPlanMeals = mealPlanMealRepository.findByMeal(meal);
                for (MealPlan_Meal mealPlanMeal : mealPlanMeals) {
                    mealPlanMeal.setMealPlan(null);
                    mealPlanMeal.setWeekDay(null);
                    mealPlanMeal.setMeal(null);
                    mealPlanMealRepository.delete(mealPlanMeal);
                }
                mealRepository.delete(meal);
                return ResponseEntity.status(HttpStatus.OK).body("Meal deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No meal found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred");
        }
    }


    public ResponseEntity<?> addNewMeal(Integer weekDayId, Authentication authentication) {
        try {
            WeekDay weekDay = weekDayRepository.findByWeekDayId(weekDayId);
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if (weekDay != null) {
                Meal meal = new Meal();
                mealRepository.save(meal);
                MealPlan_Meal mealPlan_meal = new MealPlan_Meal();
                mealPlan_meal.setMealPlan(user.getMealPlan());
                mealPlan_meal.setMeal(meal);
                mealPlan_meal.setWeekDay(weekDay);
                mealPlanMealRepository.save(mealPlan_meal);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No week day found");
            }
            return ResponseEntity.status(HttpStatus.OK).body("New meal added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    public ResponseEntity<?> removeRecipeFromMeal(Integer mealId, Integer recipeId,Authentication authentication) {
        try {
            Meal meal = mealRepository.findByMealId(mealId);
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (meal == null || recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Meal or recipe not found");
            }
            if (!meal.getMealRecipes().contains(recipe)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Meal doesn't contain this recipe");
            }
            meal.getMealRecipes().remove(recipe);
            mealRepository.save(meal);
            if (meal.getMealRecipes().isEmpty()) {
                User user = new User();
                ResponseEntity<?> responseEntity = userService.findUser(authentication);
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    user = (User) responseEntity.getBody();
                }
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
                MealPlan_Meal mealPlanMeal = mealPlanMealRepository
                        .findByMealAndMealPlan(meal, user.getMealPlan());
                if (mealPlanMeal != null) {
                    mealPlanMeal.setWeekDay(null);
                    mealPlanMeal.setMealPlan(null);
                    mealPlanMeal.setMeal(null);
                    mealPlanMealRepository.delete(mealPlanMeal);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Recipe removed from the meal");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}


