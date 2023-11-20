package project.mealPlan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;
import project.mealPlan.seeder.DatabaseSeeder;
import project.mealPlan.service.*;

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
    @Autowired
    CategoryService categoryService;
    @Autowired
    WeekDayService weekDayService;
    @Autowired
    FilterService filterService;
    @Autowired
    IngredientService ingredientService;
    /*
    @PostMapping("/seeder")
    public ResponseEntity<?> seeder() {
        try {
             return databaseSeeder.setAdmin();
            //return ResponseEntity.status(HttpStatus.CREATED).body("ok");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("/deleteRecipe")
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
    @PostMapping("/addNewCategory")
    public ResponseEntity<?> addNewCategory(@RequestBody Map<String, Object> categoryInput) {
        try {
            return categoryService.addNewCategory(categoryInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestBody Map<String, Object> categoryInput) {
        try {
            return categoryService.updateCategory(categoryInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteCategory")
    public ResponseEntity<?> deleteCategory(@RequestBody Map<String, Object> categoryInput) {
        try {
            return categoryService.deleteCategory(categoryInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addNewIngredient")
    public ResponseEntity<?> addNewIngredient(@RequestBody Map<String, Object> ingredientInput) {
        try {
            return ingredientService.addNewIngredient(ingredientInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/updateIngredient")
    public ResponseEntity<?> updateIngredient(@RequestBody Map<String, Object> ingredientInput) {
        try {
            return ingredientService.updateIngredient(ingredientInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteIngredient")
    public ResponseEntity<?> deleteIngredient(@RequestBody Map<String, Object> ingredientInput) {
        try {
            return  ingredientService.deleteIngredient(ingredientInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addNewFilter")
    public ResponseEntity<?> addNewFilter(@RequestBody Map<String, Object> filterInput) {
        try {
            return filterService.addNewFilter(filterInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateFilter")
    public ResponseEntity<?> updateFilter(@RequestBody Map<String, Object> filterInput) {
        try {
            return filterService.updateFilter(filterInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteFilter")
    public ResponseEntity<?> deleteFilter(@RequestBody Map<String, Object> filterInput) {
        try {
            return  filterService.deleteFilter(filterInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
