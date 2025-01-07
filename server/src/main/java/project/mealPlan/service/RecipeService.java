package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;
import org.springframework.http.ResponseEntity;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
public class RecipeService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    FilterRepository filterRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Recipe_IngredientRepository recipeIngredientRepository;
    @Autowired
    UserService userService;
    @Autowired
    InitializationStatusRepository initializationStatusRepository;
    @Autowired
    MealRepository mealRepository;

    @Transactional
    public ResponseEntity<?> addRecipe(Map<String, Object> recipeInput, Authentication authentication) {
        try {
            // Validate recipe name
            String recipeName = (String) recipeInput.get("recipeName");
            if (recipeName == null || recipeName.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Recipe name cannot be empty");
            }
    
            // Validate categories
            List<String> categoryNames = (List<String>) recipeInput.get("categories");
        
            // Validate filters
            List<String> filterNames = (List<String>) recipeInput.get("filters");
        
    
            // Validate ingredients
            List<Map<String, Object>> ingredientInputs = (List<Map<String, Object>>) recipeInput.get("ingredients");
            if (ingredientInputs == null || ingredientInputs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("At least one ingredient is required");
            }
    
            // Process ingredients
            List<Recipe_Ingredient> recipeIngredients = new ArrayList<>();
            for (Map<String, Object> ingredientInput : ingredientInputs) {
                String ingredientName = (String) ingredientInput.get("ingredientName");
                if (ingredientName == null || ingredientName.trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ingredient name cannot be empty");
                }
    
    
                Double quantity = Double.parseDouble(ingredientInput.get("quantity").toString());
                if (quantity == null || quantity <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ingredient quantity must be a positive number and not null");
                }
    
                String unitName = (String) ingredientInput.get("unit");
             
                Ingredient existingIngredient = ingredientRepository.findByIngredientName(ingredientName);
                if (existingIngredient != null) {
                    Recipe_Ingredient newRecipeIngredient = new Recipe_Ingredient();
                    newRecipeIngredient.setIngredient(existingIngredient);
                    Unit unit = unitRepository.findByUnitName(unitName);
                    if (unit != null) {
                        newRecipeIngredient.setUnit(unit);
                    } else {
                        unit = unitRepository.findByUnitName("notKnow");
                        newRecipeIngredient.setUnit(unit);
                    }
                    if (quantity != null && quantity > 0) {
                        newRecipeIngredient.setQuantity(quantity);
                    }
                    recipeIngredients.add(newRecipeIngredient);
                }
            }
           
            // Validate user authentication
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
    
            // Prepare recipe
            Recipe newRecipe = new Recipe(recipeName);
            newRecipe.setRecipeIngredients(recipeIngredients);
            newRecipe.setUser(user);
            String notes = (String) recipeInput.get("notes");

            // Validate notes to ensure it's not null or empty
            if (notes == null || notes.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Notes cannot be empty");
            } else {
                newRecipe.setNotes(notes);
            }
    
            // Validate calories (optional)
            Integer calories = (Integer) recipeInput.get("calories");
            if (calories != null && calories < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Calories cannot be negative");
            }
            newRecipe.setCalories(calories);
    
            // Process file (image)
            MultipartFile file = (MultipartFile) recipeInput.get("file");
            if (file != null) {
                // Validate file type (optional)
                if (!file.getContentType().startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid file type. Only image files are allowed.");
                }
    
                byte[] imageData = ImageUtilService.compressImage(file.getBytes());
                newRecipe.setRecipeImageData(imageData);
                newRecipe.setRecipeImageName(file.getOriginalFilename());
            }
    
            // Save recipe and associate it with the user
            recipeRepository.save(newRecipe);
            user.getUserRecipes().add(newRecipe);
            userRepository.save(user);
    
            return ResponseEntity.status(HttpStatus.CREATED).body(newRecipe.getRecipeId());
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    
    @Transactional
    public ResponseEntity<?> addRecipeAdmin(Map<String, Object> recipeInput) {
        try {
            // Validate recipe name
            String recipeName = (String) recipeInput.get("recipeName");
            if (recipeName == null || recipeName.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Recipe name cannot be empty");
            }
    
            // Validate calories (optional, but can be an integer or null)
            Integer calories = (Integer) recipeInput.get("calories");
    

            List<String> categoryNames = (List<String>) recipeInput.get("categories");
            List<String> filterNames = (List<String>) recipeInput.get("filters");
           
    
            // Validate ingredients
            List<Map<String, Object>> ingredientInputs = (List<Map<String, Object>>) recipeInput.get("ingredients");
            if (ingredientInputs == null || ingredientInputs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("At least one ingredient is required");
            }
    
            for (Map<String, Object> ingredientInput : ingredientInputs) {
                String ingredientName = (String) ingredientInput.get("ingredientName");
                if (ingredientName == null || ingredientName.trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ingredient name cannot be empty");
                }
    
                String unitName = (String) ingredientInput.get("unit");
           
    
                Double quantity = Double.parseDouble(ingredientInput.get("quantity").toString());
                if (quantity == null || quantity <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ingredient quantity must be a positive number");
                }
            }
    
            // Proceed with recipe creation
            Recipe newRecipe = new Recipe(recipeName);
            
            // Categories handling (save or link to existing categories)
            for (String categoryName : categoryNames) {
                Category existingCategory = categoryRepository.findByRecipeCategoryName(categoryName);
                if (existingCategory != null) {
                    newRecipe.getRecipeCategories().add(existingCategory);
                } else {
                    Category newCategory = new Category(categoryName);
                    categoryRepository.save(newCategory);
                    newRecipe.getRecipeCategories().add(newCategory);
                }
            }
    
            // Filters handling (save or link to existing filters)
            for (String filterName : filterNames) {
                Filter existingFilter = filterRepository.findByRecipeFilterName(filterName);
                if (existingFilter != null) {
                    newRecipe.getRecipeFilters().add(existingFilter);
                } else {
                    Filter newFilter = new Filter(filterName);
                    filterRepository.save(newFilter);
                    newRecipe.getRecipeFilters().add(newFilter);
                }
            }
    
            // Ingredients handling (save or link to existing ingredients)
            List<Recipe_Ingredient> recipeIngredients = new ArrayList<>();
            for (Map<String, Object> ingredientInput : ingredientInputs) {
                String ingredientName = (String) ingredientInput.get("ingredientName");
                String unitName = (String) ingredientInput.get("unit");
                Double quantity = Double.parseDouble(ingredientInput.get("quantity").toString());
    
                Ingredient existingIngredient = ingredientRepository.findByIngredientName(ingredientName);
                if (existingIngredient != null) {
                    Recipe_Ingredient newRecipeIngredient = new Recipe_Ingredient();
                    newRecipeIngredient.setIngredient(existingIngredient);
                    Unit unit = unitRepository.findByUnitName(unitName);
                    if (unit != null) {
                        newRecipeIngredient.setUnit(unit);
                    } else {
                        Unit newUnit = new Unit(unitName);
                        newRecipeIngredient.setUnit(newUnit);
                        unitRepository.save(newUnit);
                    }
                    if (quantity != null && quantity > 0) {
                        newRecipeIngredient.setQuantity(quantity);
                    }
                    recipeIngredients.add(newRecipeIngredient);
                } else {
                    Ingredient newIngredient = new Ingredient(ingredientName);
                    ingredientRepository.save(newIngredient);
                    Recipe_Ingredient newRecipeIngredient = new Recipe_Ingredient();
                    newRecipeIngredient.setIngredient(newIngredient);
                    Unit unit = unitRepository.findByUnitName(unitName);
                    if (unit != null) {
                        newRecipeIngredient.setUnit(unit);
                    } else {
                        Unit newUnit = new Unit(unitName);
                        newRecipeIngredient.setUnit(newUnit);
                        unitRepository.save(newUnit);
                    }
                    if (quantity != null && quantity > 0) {
                        newRecipeIngredient.setQuantity(quantity);
                    }
                    recipeIngredients.add(newRecipeIngredient);
                }
            }
            newRecipe.setRecipeIngredients(recipeIngredients);
    
            // Set user and other properties
            User user = userRepository.findUserByName("ADMIN");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
                newRecipe.setUser(user);
            String notes = (String) recipeInput.get("notes");
    
            // Validate notes to ensure it's not null or empty
            if (notes == null || notes.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Notes cannot be empty");
            } else {
                newRecipe.setNotes(notes);
            }
            if (calories != null) {
                newRecipe.setCalories(calories);
            }
    
            // Save the new recipe
            recipeRepository.save(newRecipe);
            return ResponseEntity.status(HttpStatus.OK).body(newRecipe.getRecipeId());
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    
    
    @Transactional
    public ResponseEntity<?> updateRecipe(Map<String, Object> recipeUpdate) {
        try {
            Integer recipeId = (Integer) recipeUpdate.get("recipeId");
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Recipe not found");
            }
    
            // Validate and update recipeName
         
                String recipeName = (String) recipeUpdate.get("recipeName");
                if (recipeName == null || recipeName.trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe name cannot be empty");
                }
                recipe.setRecipeName(recipeName);
            
    
            // Validate and update calories
            if (recipeUpdate.containsKey("calories")) {
                Integer calories = (Integer) recipeUpdate.get("calories");
                if (calories != null && calories < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Calories cannot be negative");
                }
                recipe.setCalories(calories);
            }
    
            // Validate and update notes
            String notes = (String) recipeUpdate.get("notes");
            if (notes == null || notes.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Notes cannot be empty");
            }
            recipe.setNotes(notes);
    
            
    
            // Validate and update categories
            if (recipeUpdate.containsKey("categories")) {
                List<String> categoryNames = (List<String>) recipeUpdate.get("categories");
                List<Category> updatedCategories = new ArrayList<>();
                for (String categoryName : categoryNames) {
                    if (categoryName == null || categoryName.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name cannot be empty");
                    }
                    Category existingCategory = categoryRepository.findByRecipeCategoryName(categoryName);
                    if (existingCategory != null) {
                        updatedCategories.add(existingCategory);
                    }
                }
                recipe.setRecipeCategories(updatedCategories);
            }
    
            // Validate and update filters
            if (recipeUpdate.containsKey("filters")) {
                List<String> filterNames = (List<String>) recipeUpdate.get("filters");
                List<Filter> updatedFilters = new ArrayList<>();
                for (String filterName : filterNames) {
                    if (filterName == null || filterName.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Filter name cannot be empty");
                    }
                    Filter existingFilter = filterRepository.findByRecipeFilterName(filterName);
                    if (existingFilter != null) {
                        updatedFilters.add(existingFilter);
                    }
                }
                recipe.setRecipeFilters(updatedFilters);
            }
    
            
                List<Map<String, Object>> ingredientInputs = (List<Map<String, Object>>) recipeUpdate.get("ingredients");
                if (ingredientInputs == null || ingredientInputs.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one ingredient is required");
                }
    
                List<Recipe_Ingredient> updatedIngredients = new ArrayList<>();
                for (Map<String, Object> ingredientInput : ingredientInputs) {
                    String ingredientName = (String) ingredientInput.get("ingredientName");
                    if (ingredientName == null || ingredientName.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingredient name cannot be empty");
                    }
    
                    Double quantity = Double.parseDouble(ingredientInput.get("quantity").toString());
                    if (quantity == null || quantity <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingredient quantity must be a positive number and not null");
                    }
    
                    String unitName = (String) ingredientInput.get("unit");
                    if (unitName == null || unitName.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingredient unit cannot be empty");
                    }
                    Ingredient existingIngredient = ingredientRepository.findByIngredientName(ingredientName);
                    if (existingIngredient != null) {
                        Recipe_Ingredient updatedRecipeIngredient = null;
                        for (Recipe_Ingredient recipeIngredient : recipe.getRecipeIngredients()) {
                            if (recipeIngredient.getIngredient().equals(existingIngredient)) {
                                updatedRecipeIngredient = recipeIngredient;
                                break;
                            }
                        }
                        if (updatedRecipeIngredient == null) {
                            updatedRecipeIngredient = new Recipe_Ingredient();
                            updatedRecipeIngredient.setIngredient(existingIngredient);
                        }
    
                        Unit unit = unitRepository.findByUnitName(unitName);
                        if (unit != null) {
                            updatedRecipeIngredient.setUnit(unit);
                        } else {
                            unit = unitRepository.findByUnitName("notKnow");
                            updatedRecipeIngredient.setUnit(unit);
                        }
    
                        updatedRecipeIngredient.setQuantity(quantity);
                        updatedIngredients.add(updatedRecipeIngredient);
                    }
                }
    
                // Remove old ingredients
                List<Recipe_Ingredient> ingredientsToRemove = new ArrayList<>(recipe.getRecipeIngredients());
                ingredientsToRemove.removeAll(updatedIngredients);
                
                for (Recipe_Ingredient ingredientToRemove : ingredientsToRemove) {
                    ingredientToRemove.setIngredient(null);
                    ingredientToRemove.setUnit(null);
                }
                
                // Usuwanie składników z bazy danych
                recipeIngredientRepository.deleteAll(ingredientsToRemove);
                // Usuwamy składniki z listy w pamięci
                recipe.getRecipeIngredients().removeAll(ingredientsToRemove);
                
                // Dodanie nowych składników do listy
                recipe.getRecipeIngredients().addAll(updatedIngredients);
            
    
            // Save the updated recipe
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.OK).body("Recipe updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating recipe");
        }
    }
    
    @Transactional
    public ResponseEntity<?> deleteRecipe(Integer recipeId) {
        try {
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Recipe not found");
            }
            List<User> users = userRepository
                    .findAllByUserFavouriteRecipesContains(recipe);
            for (User user : users) {
                user.getUserFavouriteRecipes().remove(recipe);
                userRepository.save(user);
            }
            recipe.getRecipeFilters().clear();
            recipe.getRecipeCategories().clear();
            List<Recipe_Ingredient> ingredientsToDelete = new ArrayList<>();
            if (recipe.getRecipeIngredients() != null) {
                ingredientsToDelete.addAll(recipe.getRecipeIngredients());
            } for (Recipe_Ingredient ingredient : ingredientsToDelete) {
                ingredient.setIngredient(null);
                ingredient.setUnit(null);
                recipe.getRecipeIngredients().remove(ingredient);
            }
        List<Meal> meals = mealRepository.findByMealRecipes(recipe);
            for(Meal meal : meals){
                meal.getMealRecipes().remove(recipe);
                mealRepository.save(meal);
            }
            if(ingredientsToDelete != null) {
                recipeIngredientRepository.deleteAll(ingredientsToDelete);
            }
            recipeRepository.delete(recipe);
            return ResponseEntity.status(HttpStatus.OK).body("Recipe deleted");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting recipe");
        }
    }
    @Transactional
    public ResponseEntity<?> addRecipeImage(Integer recipeId,
                                            MultipartFile file) {
        try {
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Recipe not found");
            }
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Uploaded file is not an image");
            }
            byte[] imageData = ImageUtilService.compressImage
                    (file.getBytes());

            recipe.setRecipeImageData(imageData);
            recipe.setRecipeImageName(file.getOriginalFilename());
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Image uploaded and compressed successfully."
                            + recipeId + " " +file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading and compressing image.");
        }
    }
    public ResponseEntity<?> getAllRecipes()
    {
        try {
            List<Recipe> recipes = recipeRepository.findAll();
            List<Map<String, Object>> recipeInfoList = new ArrayList<>();
            for (Recipe recipe : recipes) {
                Map<String, Object> recipeInfo = new HashMap<>();
                recipeInfo.put("recipeId", recipe.getRecipeId());
                recipeInfo.put("recipeName", recipe.getRecipeName());
                if(recipe.getRecipeImageData() != null) {
                    byte[] imageData = ImageUtilService.decompressImage(recipe.getRecipeImageData());
                   // byte[] imageData = recipe.getRecipeImageData();
                    String base64ImageData = Base64.getEncoder().encodeToString(imageData);
                    recipeInfo.put("imageData", base64ImageData);
                }
                recipeInfoList.add(recipeInfo);
            }
            return new ResponseEntity<>(recipeInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> getRecipeData(Integer recipeId, Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Map<String, Object> recipeInfo;
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            recipeInfo = new HashMap<>();
            recipeInfo.put("recipeId", recipe.getRecipeId());
            recipeInfo.put("recipeName", recipe.getRecipeName());
            recipeInfo.put("notes", recipe.getNotes());
            if(user.getHiddenCalories() != TRUE) {
                recipeInfo.put("calories", recipe.getCalories());
            }
             if(recipe.getRecipeImageName() != null) {
                recipeInfo.put("imageName", recipe.getRecipeImageName());
            }
            if (recipe.getRecipeImageData() != null) {
                byte[] imageData = ImageUtilService.decompressImage(recipe.getRecipeImageData());
                String base64ImageData = Base64.getEncoder().encodeToString(imageData);
                recipeInfo.put("imageData", base64ImageData);
            }
            List<String> categories = new ArrayList<>();
            for (Category category : recipe.getRecipeCategories()) {
                categories.add(category.getRecipeCategoryName());
            }
            recipeInfo.put("categories", categories);
            List<String> filters = new ArrayList<>();
            for (Filter filter : recipe.getRecipeFilters()) {
                filters.add(filter.getRecipeFilterName());
            }
            recipeInfo.put("filters", filters);
            List<Map<String, Object>> ingredientsList = new ArrayList<>();
            for (Recipe_Ingredient recipeIngredient : recipe.getRecipeIngredients()) {
                Map<String, Object> ingredientInfo = new HashMap<>();
                ingredientInfo.put("ingredientName", recipeIngredient.getIngredient().getIngredientName());
                if (recipeIngredient.getQuantity() != null) {
                    ingredientInfo.put("quantity", recipeIngredient.getQuantity());
                }
                if (recipeIngredient.getUnit() != null) {
                    ingredientInfo.put("unit", recipeIngredient.getUnit().getUnitName());
                }
                ingredientsList.add(ingredientInfo);
            }
            recipeInfo.put("ingredients", ingredientsList);

            return new ResponseEntity<>(recipeInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error receiving recipe data",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> getRecipesByCategoriesAndFilters(
            List<Category> categories, List<Filter> filters, Integer type, boolean hideAllergens, Authentication authentication
    ) {
        try {
            List<Recipe> allRecipes = new ArrayList<>();
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            switch (type) {
                case 1: // Favourite recipes
                    allRecipes = user.getUserFavouriteRecipes();
                    break;
                case 2: // Preferred ingredients
                    List<Ingredient> preferredIngredients = user.getUserPreferredIngredients();
                    if (preferredIngredients != null && !preferredIngredients.isEmpty()) {
                        allRecipes = recipeRepository.findAll();
                        allRecipes = allRecipes.stream()
                                .filter(recipe -> recipe.getRecipeIngredients().stream()
                                        .anyMatch(ri -> preferredIngredients.contains(ri.getIngredient())))
                                .collect(Collectors.toList());
                    }break;
                case 3: // User recipes
                    allRecipes = user.getUserRecipes();
                    break;
                default:
                    allRecipes = recipeRepository.findAll();
                    break;
            }
            if(hideAllergens && user.getUserAllergenIngredients() != null
                    && !user.getUserAllergenIngredients().isEmpty())
            {
                List<Ingredient> userAllergens = user.getUserAllergenIngredients();
                allRecipes = allRecipes.stream()
                        .filter(recipe -> recipe.getRecipeIngredients().stream()
                                .noneMatch(ri -> userAllergens.contains(ri.getIngredient())))
                        .collect(Collectors.toList());
            }
            List<Map<String, Object>> recipesData = new ArrayList<>();
            if (categories != null && !categories.isEmpty()) {
                allRecipes = allRecipes.stream()
                        .filter(recipe -> recipe.getRecipeCategories().containsAll(categories))
                        .collect(Collectors.toList());
            }
            if (filters != null && !filters.isEmpty()) {
                allRecipes = allRecipes.stream()
                        .filter(recipe -> recipe.getRecipeFilters().containsAll(filters))
                        .collect(Collectors.toList());
            }
            recipesData = allRecipes.stream()
                    .map(recipe -> {
                        Map<String, Object> recipeData = new HashMap<>();
                        recipeData.put("recipeId", recipe.getRecipeId());
                        recipeData.put("recipeName", recipe.getRecipeName());
                        if(recipe.getRecipeImageData() != null) {
                           byte[] imageData = ImageUtilService.decompressImage(recipe.getRecipeImageData());
                            //byte[] imageData = recipe.getRecipeImageData();
                            String base64ImageData = Base64.getEncoder().encodeToString(imageData);
                            recipeData.put("imageData", base64ImageData);
                        }else{
                            recipeData.put("imageData", null);
                        }
                        return recipeData;
                    }).collect(Collectors.toList());

            if (recipesData.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(recipesData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public ResponseEntity<?> addToFavouriteRecipes(Integer recipeId,
                                                   Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe != null) {
                List<Recipe> userFavoriteRecipes = user.getUserFavouriteRecipes();
                if (!userFavoriteRecipes.contains(recipe)) {
                    userFavoriteRecipes.add(recipe);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Recipe added to favorites");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Recipe is already in favorites");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding recipe to favorites");
        }
    }
    public ResponseEntity<?> deleteFromFavouriteRecipes(Integer recipeId,
                                                        Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe != null) {
                List<Recipe> userFavoriteRecipes = user.getUserFavouriteRecipes();
                if (userFavoriteRecipes.contains(recipe)) {
                    userFavoriteRecipes.remove(recipe);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Recipe deleted from favorites");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Recipe not in the favorites list");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recipe not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting recipe from favorites");
        }
    }

    public  ResponseEntity<?> getFavouriteRecipes(Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            List<Recipe> userFavoriteRecipes = user.getUserFavouriteRecipes();
            List<Map<String, Object>> recipeInfoList = new ArrayList<>();
            if (!userFavoriteRecipes.isEmpty()) {
                for (Recipe recipe : userFavoriteRecipes) {
                    Map<String, Object> recipeInfo = new HashMap<>();
                    recipeInfo.put("recipeId", recipe.getRecipeId());
                    recipeInfo.put("recipeName", recipe.getRecipeName());
                    if (recipe.getRecipeImageData() != null) {
                        byte[] imageData = ImageUtilService.decompressImage(recipe.getRecipeImageData());
                        String base64ImageData = Base64.getEncoder().encodeToString(imageData);
                        recipeInfo.put("imageData", base64ImageData);
                    }
                    recipeInfoList.add(recipeInfo);
                }
            }
            return new ResponseEntity<>(recipeInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error receiving recipes from favorites");
        }
    }
    public  ResponseEntity<?> getUserRecipes(Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            List<Recipe> userRecipes = user.getUserRecipes();
            List<Map<String, Object>> recipeInfoList = new ArrayList<>();
            if (!userRecipes.isEmpty()) {
                for (Recipe recipe : userRecipes) {
                    Map<String, Object> recipeInfo = new HashMap<>();
                    recipeInfo.put("recipeId", recipe.getRecipeId());
                    recipeInfo.put("recipeName", recipe.getRecipeName());
                    if (recipe.getRecipeImageData() != null) {
                        byte[] imageData = ImageUtilService.decompressImage(recipe.getRecipeImageData());
                        String base64ImageData = Base64.getEncoder().encodeToString(imageData);
                        recipeInfo.put("imageData", base64ImageData);
                    }
                    recipeInfoList.add(recipeInfo);
                }
            }
            return new ResponseEntity<>(recipeInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe to favorites");
        }
    }
    public ResponseEntity<?> updateUserRecipe(Map<String, Object> recipeUpdate,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Integer recipeId = (Integer) recipeUpdate.get("recipeId");
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe != null) {
                List<Recipe> userRecipes = user.getUserRecipes();
                if (userRecipes.contains(recipe)) {
                    updateRecipe(recipeUpdate);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Recipe updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating recipe");
        }
    }
    @Transactional
    public ResponseEntity<?> deleteUserRecipe(Integer recipeId,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe != null) {
                List<Recipe> userRecipes = user.getUserRecipes();
                if (userRecipes.contains(recipe)) {
                    userRecipes.remove(recipe);
                    deleteRecipe(recipeId);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("Recipe deleted");
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not the owner of the recipe");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleted recipe");
        }
    }
}



