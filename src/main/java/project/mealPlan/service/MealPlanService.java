package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MealPlanService {
    @Autowired
    WeekDayRepository weekDayRepository;
    @Autowired
    MealPlan_MealRepository mealPlanMealRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MealRepository mealRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    MealPlanRepository mealPlanRepository;
    @Autowired
    MealService mealService;

    @Transactional
    public ResponseEntity<?> addRecipeToMealAndMealPlan(Map<String, Object> mealData) {
        try {
            User user = userRepository.findUserByName("test");
            Integer recipeId = (Integer) mealData.get("recipeId");
            Integer weekDayId = (Integer) mealData.get("weekDayId");
            Integer mealId = (Integer) mealData.get("mealId");
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            MealPlan mealPlan = user.getMealPlan();
            WeekDay weekDay = weekDayRepository.findByWeekDayId(weekDayId);

            if (mealPlan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meal plan not found");
            }
            Meal meal = null;
            if (mealId != null && mealId > 0) {
                meal = mealRepository.findByMealId(mealId);
            }
            if (weekDayId == null && meal != null) {
                return mealService.addRecipeToMeal(mealData);
            }
            MealPlan_Meal existingMealPlanMeal = mealPlanMealRepository.findByMealAndWeekDayAndMealPlan(meal, weekDay, mealPlan);
            if (existingMealPlanMeal != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This meal is already added to this day");
            } else {
                if (meal == null && mealId == 0) {
                    meal = new Meal();
                }
                if (recipe != null) {
                    if (!meal.getMealRecipes().contains(recipe)) {
                        meal.getMealRecipes().add(recipe);
                        mealRepository.save(meal);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meal already contains this recipe");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding recipe to meal");
                }
                MealPlan_Meal newMealPlanMeal = new MealPlan_Meal();
                newMealPlanMeal.setMealPlan(mealPlan);
                newMealPlanMeal.setMeal(meal);
                newMealPlanMeal.setWeekDay(weekDay);

                mealPlanMealRepository.save(newMealPlanMeal);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Recipe added to meal and meal added to meal plan");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    public ResponseEntity<?> getMealsByWeekDay() {
        try {
            User user = userRepository.findUserByName("test");
            MealPlan mealPlan = user.getMealPlan();
            if (mealPlan != null) {
                List<MealPlan_Meal> mealPlanMeals = mealPlan.getMealPlanMeals();
                Map<String, List<Map<String, Object>>> mealsByWeekDay = new HashMap<>();
                for (MealPlan_Meal mealPlanMeal : mealPlanMeals) {
                    String weekDayName = mealPlanMeal.getWeekDay().getWeekDayName();
                    mealsByWeekDay.putIfAbsent(weekDayName, new ArrayList<>());
                    Map<String, Object> mealDetails = new HashMap<>();
                    mealDetails.put("mealId", mealPlanMeal.getMeal().getMealId());

                    List<Recipe> recipesInMeal = mealPlanMeal.getMeal().getMealRecipes();
                    List<Map<String, Object>> recipeDetails = new ArrayList<>();
                    for (Recipe recipe : recipesInMeal) {
                        Map<String, Object> recipeInfo = new HashMap<>();
                        recipeInfo.put("recipeId", recipe.getRecipeId());
                        recipeInfo.put("recipeName", recipe.getRecipeName());
                        recipeDetails.add(recipeInfo);
                    }
                    mealDetails.put("recipes", recipeDetails);
                    mealsByWeekDay.get(weekDayName).add(mealDetails);
                }
                return ResponseEntity.status(HttpStatus.OK).body(mealsByWeekDay);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meal Plan not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    public ResponseEntity<?> generateShoppingList() {
        try {
            User user = userRepository.findUserByName("test");
            MealPlan mealPlan = user.getMealPlan();
            if (mealPlan != null) {
                List<MealPlan_Meal> mealPlanMeals = mealPlan.getMealPlanMeals();
                Map<String, List<Map<String, Object>>> shoppingList = new HashMap<>();
                for (MealPlan_Meal mealPlanMeal : mealPlanMeals) {
                    List<Recipe> recipesInMeal = mealPlanMeal.getMeal().getMealRecipes();
                    for (Recipe recipe : recipesInMeal) {
                        for (Recipe_Ingredient recipeIngredient : recipe.getRecipeIngredients()) {
                            String ingredientName = recipeIngredient.getIngredient().getIngredientName();
                            Double ingredientQuantity = recipeIngredient.getQuantity();
                            String ingredientUnit = (recipeIngredient.getUnit() != null) ? recipeIngredient.getUnit().getUnitName() : null;
                            if (shoppingList.containsKey(ingredientName)) {
                                List<Map<String, Object>> existingIngredients = shoppingList.get(ingredientName);
                                boolean ingredientUpdated = false;
                                for (Map<String, Object> existingIngredient : existingIngredients) {
                                    if (ingredientUnit != null && ingredientUnit.equals(existingIngredient.get("unit"))) {
                                        Double existingQuantity = (Double) existingIngredient.get("quantity");
                                        existingIngredient.put("quantity", existingQuantity + ingredientQuantity);
                                        ingredientUpdated = true;
                                        break;
                                    }
                                }
                                if (!ingredientUpdated) {
                                    Map<String, Object> newIngredient = new HashMap<>();
                                    newIngredient.put("quantity", ingredientQuantity);
                                    if (ingredientUnit != null) {
                                        newIngredient.put("unit", ingredientUnit);
                                    }
                                    existingIngredients.add(newIngredient);
                                }
                            } else {
                                List<Map<String, Object>> newIngredientsList = new ArrayList<>();
                                Map<String, Object> newIngredient = new HashMap<>();
                                newIngredient.put("quantity", ingredientQuantity);
                                if (ingredientUnit != null) {
                                    newIngredient.put("unit", ingredientUnit);
                                }
                                newIngredientsList.add(newIngredient);
                                shoppingList.put(ingredientName, newIngredientsList);
                            }
                        }
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(shoppingList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meal Plan not found; Error creating shopping list");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    public ResponseEntity<?> shoppingListStatus(Boolean change) {
        try {
            User user = userRepository.findUserByName("test");
            MealPlan mealPlan = user.getMealPlan();
            if (mealPlan != null) {
                boolean shoppingListStatus = mealPlan.getShoppingListStatus();
                if (change) {
                    mealPlan.setShoppingListStatus(!shoppingListStatus);
                }
                mealPlanRepository.save(mealPlan);
                return ResponseEntity.status(HttpStatus.OK).body(mealPlan.getShoppingListStatus());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shopping list not found");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    public ResponseEntity<?> shoppingListStatus() {
        try {
            User user = userRepository.findUserByName("test");
            if(user != null) {
                return new ResponseEntity<>(user.getMealPlan().getShoppingListStatus(), HttpStatus.OK);
            }
            else return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>
                    ("Error receiving shopping list status ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
