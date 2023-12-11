package project.mealPlan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;
import project.mealPlan.service.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class adminController {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    WeekDayRepository weekDayRepository;
    @Autowired
    RecipeService recipeService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FilterService filterService;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    UnitService unitService;
    @Autowired
    UserService userService;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @PostMapping("/addNewUnit")
    public ResponseEntity<?> addNewUnit(@RequestBody Map<String, Object> unitInput) {
        try {
            return unitService.addNewUnit(unitInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateUnit")
    public ResponseEntity<?> updateUnit(@RequestBody Map<String, Object> unitInput) {
        try {
            return unitService.updateUnit(unitInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteUnit")
    public ResponseEntity<?> deleteUnit(@RequestBody Map<String, Object> unitInput) {
        try {
            return unitService.deleteUnit(unitInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Object> userInput) {
        try {
            return userService.deleteUser(userInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/blockUser")
    public ResponseEntity<?> blockUser(@RequestBody Map<String, Object> userInput) {
        try {
            return userService.blockUser(userInput);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
