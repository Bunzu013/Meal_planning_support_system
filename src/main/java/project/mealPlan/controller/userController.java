package project.mealPlan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.mealPlan.configuration.JwtTokenUtil;
import project.mealPlan.entity.*;
import project.mealPlan.repository.MealPlanRepository;
import project.mealPlan.repository.MealRepository;
import project.mealPlan.repository.RoleRepository;
import project.mealPlan.repository.UserRepository;
import project.mealPlan.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private MealService mealService;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private MealPlanService mealPlanService;

    @GetMapping("/getUserData")
    public ResponseEntity<?> showUserData() {
        try {
            return userService.getUserData();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/updateUserData")
    public ResponseEntity<?> editUserData(@RequestBody Map<String,Object> userInfo) {
        try {
            return userService.editUserData(userInfo);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/addRecipe")
    public ResponseEntity<?> addRecipe(@RequestBody Map<String, Object> requestData) {
        try {
            return recipeService.addRecipe(requestData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    @PostMapping("/recipes/image")
    public ResponseEntity<?> addRecipeImage(@RequestParam Integer recipeId,
                                            @RequestParam("file") MultipartFile file) {
        try {
            return  recipeService.addRecipeImage(recipeId,file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding image");
        }
    }
    @PostMapping("/updateUserRecipe")
    public ResponseEntity<?> updateUserRecipe(@RequestBody Map<String, Object> requestData) {
        try {
            return recipeService.updateUserRecipe(requestData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    @PostMapping("/deleteUserRecipe")
    public ResponseEntity<?> deleteUserRecipe(@RequestParam Integer recipeId) {
        try {
            return recipeService.deleteUserRecipe(recipeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }

    @PostMapping("/addToPreferred")
    public ResponseEntity<?> addIngredientToPreferred(@RequestParam Integer ingredientId) {
        try {
            return ingredientService.addToPreferred(ingredientId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/deleteFromPreferredIngredients")
    public ResponseEntity<?> deleteFromPreferredIngredients(@RequestParam Integer ingredientId) {
        try {
            return ingredientService.deleteFromPreferredIngredients(ingredientId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getUserAllergenIngredients")
    public ResponseEntity<?> getAllergenIngredients() {
        try {
            return ingredientService.getUserAllergens();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addToAllergenIngredients")
    public ResponseEntity<?> addAllergenIngredients(@RequestParam Integer ingredientId) {
        try {
            return ingredientService.addToAllergens(ingredientId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/deleteFromAllergenIngredients")
    public ResponseEntity<?> deleteFromAllergenIngredients(@RequestParam Integer ingredientId) {
        try {
            return ingredientService.deleteFromUserAllergens(ingredientId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getUserPreferredIngredients")
    public ResponseEntity<?> getPreferredIngredients() {
        try {
            return ingredientService.getUserPreferredIngredients();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addRecipeToFavourites")
    public ResponseEntity<?> addToFavourites(@RequestParam Integer recipeId) {
        try {
            return recipeService.addToFavourites(recipeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/deleteFromFavourites")
    public ResponseEntity<?> deleteFromFavourites(@RequestParam Integer recipeId) {
        try {
            return recipeService.deleteFromFavourites(recipeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getFavouriteRecipes")
    public ResponseEntity<?> getFavouriteRecipes() {
        try {
            return recipeService.getFavouriteRecipes();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getUserRecipes")
    public ResponseEntity<?> getUserRecipes() {
        try {
            return recipeService.getUserRecipes();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/addNewMeal")
    public ResponseEntity<?> addNewMeal() {
        try {
            Meal newMeal = new Meal();
            mealRepository.save(newMeal);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addRecipeToMeal")
    public ResponseEntity<?> addRecipeToMeal(
            @RequestBody Map<String, Object> recipeData) {
        try {
            return mealService.addRecipeToMeal(recipeData);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@PostMapping("/addRecipeToMealAndMealPlan")
    public ResponseEntity<?> addRecipeToMealAndMealPlan(@RequestBody  Map<String, Object> mealData) {
    try {
        return mealPlanService.addRecipeToMealAndMealPlan(mealData);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    @PostMapping("/deleteRecipeFromMealAndMealPlan")
    public ResponseEntity<?> addRecipeToMealAndMealPlan(@RequestParam Integer mealId,@RequestParam Integer recipeId) {
        try {
            return mealPlanService.removeRecipeFromMeal(mealId,recipeId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/getMealPlanDetails")
    public ResponseEntity<?> getMealPlanDetails() {
        try {
            return mealPlanService.getMealsByWeekDay();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/generateShoppingList")
    public ResponseEntity<?> generateShoppingList () {
        try {
            return mealPlanService.generateShoppingList();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/shoppingListStatus")
    public ResponseEntity<?> shoppingListStatus (@RequestParam boolean change) {
        try {
            return mealPlanService.shoppingListStatus(change);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/hideCalories")
    public ResponseEntity<?> hideCalories (@RequestParam Boolean hide) {
        try {
            return userService.hideCalories(hide);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
