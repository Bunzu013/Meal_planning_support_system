package project.mealPlan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.mealPlan.repository.UserRepository;
import project.mealPlan.service.*;
import java.util.*;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private MealService mealService;
    @Autowired
    private MealPlanService mealPlanService;

    @GetMapping("/getUserData")
    public ResponseEntity<?> showUserData(Authentication authentication) {
        try {
            return userService.getUserData(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/updateUserData")
    public ResponseEntity<?> editUserData(@RequestBody Map<String, Object> userInfo,Authentication authentication) {
        try {
            return userService.editUserData(userInfo,authentication);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/updatePassword")
    public ResponseEntity<?> editPassword(@RequestBody Map<String, Object> data,Authentication authentication) {
        try {
            return userService.editPassword(data,authentication);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/addRecipe")
    public ResponseEntity<?> addRecipe(@RequestBody Map<String, Object> requestData,Authentication authentication) {
        try {
            return recipeService.addRecipe(requestData,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    @PostMapping("/recipes/image")
    public ResponseEntity<?> addRecipeImage(@RequestParam Integer recipeId,
                                            @RequestParam("file") MultipartFile file
                                          ) {
        try {
            return recipeService.addRecipeImage(recipeId,file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding image");
        }
    }
    @PostMapping("/updateUserRecipe")
    public ResponseEntity<?> updateUserRecipe(@RequestBody Map<String, Object> requestData,Authentication authentication) {
        try {
            return recipeService.updateUserRecipe(requestData,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    @PostMapping("/deleteUserRecipe")
    public ResponseEntity<?> deleteUserRecipe(@RequestParam Integer recipeId,Authentication authentication) {
        try {
            return recipeService.deleteUserRecipe(recipeId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    @PostMapping("/addToPreferred")
    public ResponseEntity<?> addIngredientToPreferred(@RequestParam Integer ingredientId,Authentication authentication) {
        try {
            return ingredientService.addToPreferred(ingredientId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/deleteFromPreferredIngredients")
    public ResponseEntity<?> deleteFromPreferredIngredients(@RequestParam Integer ingredientId,Authentication authentication) {
        try {
            return ingredientService.deleteFromPreferredIngredients(ingredientId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getUserAllergenIngredients")
    public ResponseEntity<?> getAllergenIngredients(Authentication authentication) {
        try {
            return ingredientService.getUserAllergens(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addToAllergenIngredients")
    public ResponseEntity<?> addAllergenIngredients(@RequestParam Integer ingredientId,Authentication authentication) {
        try {
            return ingredientService.addToAllergens(ingredientId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/deleteFromAllergenIngredients")
    public ResponseEntity<?> deleteFromAllergenIngredients(@RequestParam Integer ingredientId,Authentication authentication) {
        try {
            return ingredientService.deleteFromUserAllergens(ingredientId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getUserPreferredIngredients")
    public ResponseEntity<?> getPreferredIngredients(Authentication authentication) {
        try {
            return ingredientService.getUserPreferredIngredients(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addRecipeToFavourites")
    public ResponseEntity<?> addToFavourites(@RequestParam Integer recipeId,Authentication authentication) {
        try {
            return recipeService.addToFavourites(recipeId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/deleteFromFavourites")
    public ResponseEntity<?> deleteFromFavourites(@RequestParam Integer recipeId,Authentication authentication) {
        try {
            return recipeService.deleteFromFavourites(recipeId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getFavouriteRecipes")
    public ResponseEntity<?> getFavouriteRecipes(Authentication authentication) {
        try {
            return recipeService.getFavouriteRecipes(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getUserRecipes")
    public ResponseEntity<?> getUserRecipes(Authentication authentication) {
        try {
            return recipeService.getUserRecipes(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
   @PostMapping("/addNewMeal")
    public ResponseEntity<?> addNewMeal(@RequestParam Integer weekDayId,Authentication authentication) {
        try {
            return mealService.addNewMeal(weekDayId,authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addRecipeToMeal")
    public ResponseEntity<?> addRecipeToMeal(
            @RequestBody Map<String, Object> recipeData,Authentication authentication) {
        try {
            return mealService.addRecipeToMeal(recipeData,authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteMeal")
    public ResponseEntity<?> deleteMeal(@RequestParam Integer mealId) {
        try {
            return mealService.deleteMeal(mealId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addRecipeToMealAndMealPlan")
    public ResponseEntity<?> addRecipeToMealAndMealPlan(@RequestBody Map<String, Object> mealData,Authentication authentication) {
        try {
            return mealPlanService.addRecipeToMealAndMealPlan(mealData,authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteRecipeFromMealAndMealPlan")
    public ResponseEntity<?> deleteRecipeToMealAndMealPlan(@RequestParam Integer mealId,
                                                           @RequestParam Integer recipeId,Authentication authentication) {
        try {
            return mealService.removeRecipeFromMeal(mealId, recipeId,authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/getMealPlanDetails")
    public ResponseEntity<?> getMealPlanDetails(Authentication authentication) {
        try {
            return mealPlanService.getMealsByWeekDay(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/generateShoppingList")
    public ResponseEntity<?> generateShoppingList(Authentication authentication) {
        try {
            return mealPlanService.generateShoppingList(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/shoppingListStatus")
    public ResponseEntity<?> shoppingListStatus(@RequestParam boolean change,Authentication authentication) {
        try {
            return mealPlanService.shoppingListStatus(change,authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getShoppingListStatus")
    public ResponseEntity<?> getShoppingListStatus(Authentication authentication)
    {
        try {
           return mealPlanService.getShoppingListStatus(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/hideCalories")
    public ResponseEntity<?> hideCalories(@RequestParam Boolean hide,Authentication authentication) {
        try {
            return userService.hideCalories(hide,authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
