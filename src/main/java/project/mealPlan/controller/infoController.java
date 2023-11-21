package project.mealPlan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.mealPlan.entity.Category;
import project.mealPlan.entity.Filter;
import project.mealPlan.repository.CategoryRepository;
import project.mealPlan.repository.FilterRepository;
import project.mealPlan.service.*;
import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllIngredients()
    {
        try {
            return ingredientService.getAllIngredients();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllFilters")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllFilters()
    {
        try {
            return filterService.getAllFilters();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllCategories")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCategories()
    {
        try {
            return categoryService.getAllCategories();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllUnits")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUnits()
    {
        try {
            return unitService.getAllUnits();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAllWeekDays")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllWeekDays()
    {
        try {
            return weekDayService.getAllWeekDays();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRecipeData/{recipeId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getRecipeData(@PathVariable Integer recipeId, Authentication authentication){
        try{
            return  recipeService.getRecipeData(recipeId,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No recipe found");
        }
    }

    @GetMapping("/getAllRecipes")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllRecipes() {
        try {
            return recipeService.getAllRecipes();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error receiving recipes");
        }
    }

    @GetMapping("/getRecipesByCategoriesAndFilters")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getRecipesByCategoriesAndFilters(
            @RequestParam(required = false) List<String> categoriesN,
            @RequestParam(required = false) List<String> filtersN,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false, defaultValue = "true") Boolean hideAllergens,
            Authentication authentication
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
            return recipeService.getRecipesByCategoriesAndFilters(categories, filters, type, hideAllergens,authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error receiving recipes");
        }
    }

}
