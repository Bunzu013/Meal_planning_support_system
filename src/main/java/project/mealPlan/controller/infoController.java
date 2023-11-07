package project.mealPlan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mealPlan.configuration.JwtTokenUtil;
import project.mealPlan.entity.Category;
import project.mealPlan.entity.Filter;
import project.mealPlan.repository.CategoryRepository;
import project.mealPlan.repository.FilterRepository;
import project.mealPlan.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class infoController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private FilterService filterService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    WeekDayService weekDayService;
    @GetMapping("/getAllIngredients")
    public ResponseEntity<?> getAllIngredients()
    {
        try {
            return ingredientService.getAllIngredients();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllFilters")
    public ResponseEntity<?> getAllFilters()
    {
        try {
            return filterService.getAllFilters();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllCategories")
    public ResponseEntity<?> getAllCategories()
    {
        try {
            return categoryService.getAllCategories();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllUnits")
    public ResponseEntity<?> getAllUnits()
    {
        try {
            return unitService.getAllUnits();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllWeekDays")
    public ResponseEntity<?> getAllWeekDays()
    {
        try {
            return weekDayService.getAllWeekDays();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRecipeData/{recipeId}")
    public ResponseEntity<?> getRecipeData(@PathVariable Integer recipeId){
        try{
            return  recipeService.getRecipeData(recipeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No recipe found");
        }
    }

    @GetMapping("/getAllRecipes")
    public ResponseEntity<?> getAllRecipes() {
        try {
            return recipeService.getAllRecipes();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error receiving recipes");
        }
    }

    @GetMapping("/getRecipesByCategoriesAndFilters")
    public ResponseEntity<?> getRecipesByCategoriesAndFilters(
            @RequestParam(required = false) List<String> categoriesN,
            @RequestParam(required = false) List<String> filtersN,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false, defaultValue = "true") Boolean hideAllergens
    ) {
        if (hideAllergens == null) {
            hideAllergens = true;
        }
        try {
            List<Category> categories = new ArrayList<>();
            List<Filter> filters = new ArrayList<>();
            if(categoriesN != null) {
                for (String categoryName : categoriesN) {
                    categories.add(categoryRepository.findByRecipeCategoryName(categoryName));
                }
            }
            if (filtersN != null) {
                for (String filterName : filtersN) {
                    filters.add(filterRepository.findByRecipeFilterName(filterName));
                }
            }
            if (type == null) {
                type = 0;
            }
            return recipeService.getRecipesByCategoriesAndFilters(categories, filters, type, hideAllergens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error w wywołaniu");
        }
    }

}
