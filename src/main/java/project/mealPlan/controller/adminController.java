package project.mealPlan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;
import project.mealPlan.seeder.DatabaseSeeder;
import project.mealPlan.service.RecipeService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class adminController {
    @Autowired
    DatabaseSeeder databaseSeeder;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    FilterRepository filterRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    WeekDayRepository weekDayRepository;
    @Autowired
    RecipeService recipeService;
/*
    @PostMapping("/seeder")
    public ResponseEntity<?> seeder() {
        try {
             return databaseSeeder.addImagesFile();
            //return ResponseEntity.status(HttpStatus.CREATED).body("ok");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("/deleteRrecipe")
    public ResponseEntity<?> deleteRecipe(@RequestParam Integer recipeId) {
        try {
            return recipeService.deleteRecipe(recipeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/updateRecipe")
    public ResponseEntity<?> updateRecipe(@RequestBody Map<String, Object> requestData) {
        try {

            return recipeService.updateRecipe(requestData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating recipe");
        }
    }
    @PostMapping("/addCategory")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        try {
            Category newCategory = new Category();
            newCategory.setRecipeCategoryName(category.getRecipeCategoryName());
            categoryRepository.save(newCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added new category: " + newCategory.getRecipeCategoryName());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addIngredient")
    public ResponseEntity<String> addIngredient(@RequestBody Ingredient ingredient) {
        try {
            Ingredient newIngredient = new Ingredient();
            newIngredient.setIngredientName(ingredient.getIngredientName());
            ingredientRepository.save(newIngredient);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added new ingredient: " + newIngredient.getIngredientName());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addFilter")
    public ResponseEntity<String> addFilter(@RequestBody Filter filter) {
        try {
            Filter newFilter = new Filter();
            newFilter.setRecipeFilterName(filter.getRecipeFilterName());
            filterRepository.save(newFilter);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added new filter: " + newFilter.getRecipeFilterName());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addUnit")
    public ResponseEntity<String> addUnit(@RequestBody Unit unit) {
        try {
            Unit newUnit = new Unit();
            newUnit.setUnitName(unit.getUnitName());
            unitRepository.save(newUnit);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added new unit: " + unit.getUnitName());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addWeekDay")
    public ResponseEntity<String> addWeekDay(@RequestBody WeekDay weekDay) {
        try {
            WeekDay newWeekDay = new WeekDay();
            newWeekDay.setWeekDayName(weekDay.getWeekDayName());
            weekDayRepository.save(newWeekDay);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added new weekDay: " + weekDay.getWeekDayName());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
