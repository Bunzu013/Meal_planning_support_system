package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Transactional
    public ResponseEntity<?> addRecipe(Map<String, Object> recipeInput) {
        try {
            String recipeName = (String) recipeInput.get("recipeName");
            Integer calories = (Integer) recipeInput.get("calories");
            List<String> categoryNames = (List<String>) recipeInput.get("categories");
            List<String> filterNames = (List<String>) recipeInput.get("filters");
            List<Map<String, Object>> ingredientInputs = (List<Map<String, Object>>) recipeInput.get("ingredients");
            String notes = (String) recipeInput.get("notes");
            Recipe newRecipe = new Recipe(recipeName);
            MultipartFile file =  (MultipartFile) recipeInput.get("file");
            for (String categoryName : categoryNames) {
                Category existingCategory = categoryRepository.findByRecipeCategoryName(categoryName);
                if (existingCategory != null) {
                    newRecipe.addCategory(existingCategory);
                }
            }
            for (String filterName : filterNames) {
                Filter existingFilter = filterRepository.findByRecipeFilterName(filterName);
                if (existingFilter != null) {
                    newRecipe.addFilter(existingFilter);
                }
            }
            List<Recipe_Ingredient> recipeIngredients = new ArrayList<>();
            for (Map<String, Object> ingredientInput : ingredientInputs) {
                String ingredientName = (String) ingredientInput.get("ingredientName");
                String unitName = (String) ingredientInput.get("unitName");
                Double quantity = Double.parseDouble(ingredientInput.get("quantity").toString());
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
                    if(quantity != null) {
                        newRecipeIngredient.setQuantity(quantity);
                    }
                    recipeIngredients.add(newRecipeIngredient);
                }
            }
            newRecipe.setRecipeIngredients(recipeIngredients);
            User user = userRepository.findUserByName("test");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User 'test' not found");
            }
            newRecipe.setUser(user);
            if(notes != null ){
                newRecipe.setNotes(notes);
            }
            if(calories != null) {
                newRecipe.setCalories(calories);
            }
            if (file != null ) {
                byte[] imageData = ImageUtilService.compressImage(file.getBytes());
                newRecipe.setRecipeImageData(imageData);
                newRecipe.setRecipeImageName(file.getOriginalFilename());
            }
            recipeRepository.save(newRecipe);
            user.getUserRecipes().add(newRecipe);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRecipe.getRecipeId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }
    @Transactional
    public ResponseEntity<?> addRecipeAdmin(@RequestBody Map<String, Object> recipeInput) {
        try {
            String recipeName = (String) recipeInput.get("recipeName");
            Integer calories = (Integer) recipeInput.get("calories");
            List<String> categoryNames = (List<String>) recipeInput.get("categories");
            List<String> filterNames = (List<String>) recipeInput.get("filters");
            List<Map<String, Object>> ingredientInputs = (List<Map<String, Object>>) recipeInput.get("ingredients");
            String notes = (String) recipeInput.get("notes");
            Recipe newRecipe = new Recipe(recipeName);
            for (String categoryName : categoryNames) {
                Category existingCategory = categoryRepository.findByRecipeCategoryName(categoryName);
                if (existingCategory != null) {
                    newRecipe.addCategory(existingCategory);
                }
                else{
                    Category newCategory = new Category(categoryName);
                    categoryRepository.save(newCategory);
                    newRecipe.addCategory(newCategory);
                }
            }
            for (String filterName : filterNames) {
                Filter existingFilter = filterRepository.findByRecipeFilterName(filterName);
                if (existingFilter != null) {
                    newRecipe.addFilter(existingFilter);
                }
                else{
                    Filter newFilter = new Filter(filterName);
                    filterRepository.save(newFilter);
                    newRecipe.addFilter(newFilter);
                }
            }
            List<Recipe_Ingredient> recipeIngredients = new ArrayList<>();
            for (Map<String, Object> ingredientInput : ingredientInputs) {
                String ingredientName = (String) ingredientInput.get("ingredientName");
                String unitName = (String) ingredientInput.get("unitName");
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
                    }
                    if(quantity != null) {
                        newRecipeIngredient.setQuantity(quantity);
                    }
                    recipeIngredients.add(newRecipeIngredient);

                }
                else{
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
                    if(quantity != null) {
                        newRecipeIngredient.setQuantity(quantity);
                    }
                    recipeIngredients.add(newRecipeIngredient);
                }
            }
            newRecipe.setRecipeIngredients(recipeIngredients);

            User user = userRepository.findUserByName("ADMIN");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User 'admin' not found");
            }
            newRecipe.setUser(user);
            if(notes != null ){
                newRecipe.setNotes(notes);
            }
            if(calories != null) {
                newRecipe.setCalories(calories);
            }
            MultipartFile file = (MultipartFile) recipeInput.get("file");
            if (file != null) {
                byte[] imageData = ImageUtilService.compressImage(file.getBytes());
                newRecipe.setRecipeImageData(imageData);
                newRecipe.setRecipeImageName(file.getOriginalFilename());
            }
            recipeRepository.save(newRecipe);

            return ResponseEntity.status(HttpStatus.OK).body(newRecipe.getRecipeId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding recipe");
        }
    }

    public ResponseEntity<?> updateRecipe(Map<String, Object> recipeUpdate) {
        try {
            Integer recipeId = (Integer) recipeUpdate.get("recipeId");
            MultipartFile file = (MultipartFile) recipeUpdate.get("file");
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
            }
            if (recipeUpdate.containsKey("recipeName")) {
                String recipeName = (String) recipeUpdate.get("recipeName");
                recipe.setRecipeName(recipeName);
            }
            if (recipeUpdate.containsKey("calories")) {
                Integer calories = (Integer) recipeUpdate.get("calories");
                recipe.setCalories(calories);
            }
            if (recipeUpdate.containsKey("notes")) {
                String notes = (String) recipeUpdate.get("notes");
                recipe.setNotes(notes);
            }
            if (file != null) {
                byte[] imageData = ImageUtilService.compressImage(file.getBytes());
                recipe.setRecipeImageData(imageData);
                recipe.setRecipeImageName(file.getOriginalFilename());
            }
            if (recipeUpdate.containsKey("categories")) {
                List<String> categoryNames = (List<String>) recipeUpdate.get("categories");
                List<Category> updatedCategories = new ArrayList<>();
                for (String categoryName : categoryNames) {
                    Category existingCategory = categoryRepository.findByRecipeCategoryName(categoryName);
                    if (existingCategory != null) {
                        updatedCategories.add(existingCategory);
                    }
                }
                List<Category> categoriesToRemove = new ArrayList<>(recipe.getRecipeCategories());
                categoriesToRemove.removeAll(updatedCategories);
                recipe.setRecipeCategories(updatedCategories);
            }
            if (recipeUpdate.containsKey("filters")) {
                List<String> filterNames = (List<String>) recipeUpdate.get("filters");
                List<Filter> updatedFilters = new ArrayList<>();

                for (String filterName : filterNames) {
                    Filter existingFilter = filterRepository.findByRecipeFilterName(filterName);
                    if (existingFilter != null) {
                        updatedFilters.add(existingFilter);
                    }
                }
                List<Filter> filtersToRemove = new ArrayList<>(recipe.getRecipeFilters());
                filtersToRemove.removeAll(updatedFilters);
                recipe.getRecipeFilters().removeAll(filtersToRemove);
                recipe.setRecipeFilters(updatedFilters);
            }
            if (recipeUpdate.containsKey("ingredients")) {
                List<Map<String, Object>> ingredientInputs = (List<Map<String, Object>>) recipeUpdate.get("ingredients");
                List<Recipe_Ingredient> updatedIngredients = new ArrayList<>();
                for (Map<String, Object> ingredientInput : ingredientInputs) {
                    String ingredientName = (String) ingredientInput.get("ingredientName");
                    String unitName = (String) ingredientInput.get("unit");
                    Double quantity = Double.parseDouble(ingredientInput.get("quantity").toString());
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
                        if (unitName != null) {
                            Unit unit = unitRepository.findByUnitName(unitName);
                            if (unit != null) {
                                updatedRecipeIngredient.setUnit(unit);
                            }
                        } else {
                            Unit unit = unitRepository.findByUnitName("notKnow");
                            updatedRecipeIngredient.setUnit(unit);
                        }
                        if(quantity != null) {
                            updatedRecipeIngredient.setQuantity(quantity);
                        }
                        updatedIngredients.add(updatedRecipeIngredient);
                    }
                }
                List<Recipe_Ingredient> ingredientsToRemove = new ArrayList<>(recipe.getRecipeIngredients());
                ingredientsToRemove.removeAll(updatedIngredients);
                for (Recipe_Ingredient ingredientToRemove : ingredientsToRemove) {
                    ingredientToRemove.setIngredient(null);
                    ingredientToRemove.setUnit(null);
                }
                recipe.getRecipeIngredients().removeAll(ingredientsToRemove);
                recipeIngredientRepository.deleteAll(ingredientsToRemove);
                recipe.getRecipeIngredients().addAll(updatedIngredients);
                recipeRepository.save(recipe);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Recipe updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating recipe");
        }
    }
    public ResponseEntity<?> deleteRecipe(Integer recipeId) {
        try {
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
            }
            List<User> users = userRepository.findAllByUserFavouriteRecipesContains(recipe);
            for (User user : users) {
                user.getUserFavouriteRecipes().remove(recipe);
                userRepository.save(user);
            }
            recipe.getRecipeFilters().clear();
            recipe.getRecipeCategories().clear();
            List<Recipe_Ingredient> ingredientsToDelete = new ArrayList<>(recipe.getRecipeIngredients());
            for (Recipe_Ingredient ingredient : ingredientsToDelete) {
                ingredient.setIngredient(null);
                ingredient.setUnit(null);
                recipe.getRecipeIngredients().remove(ingredient);
            }
            recipeIngredientRepository.deleteAll(ingredientsToDelete);
            recipeRepository.delete(recipe);
            return ResponseEntity.status(HttpStatus.OK).body("Recipe deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting recipe");
        }
    }
    @Transactional
    public ResponseEntity<?> addRecipeImage(Integer recipeId, MultipartFile file) {
        try {
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
            }
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Uploaded file is not an image");
            }
            byte[] imageData = ImageUtilService.compressImage(file.getBytes());
            recipe.setRecipeImageData(imageData);
            recipe.setRecipeImageName(file.getOriginalFilename());
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Image uploaded and compressed successfully.");
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
    public ResponseEntity<?> getRecipeData(Integer recipeId) {
        try {
            User user = userRepository.findUserByName("test");

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
            List<Category> categories, List<Filter> filters, Integer type, boolean hideAllergens
    ) {
        try {
            List<Recipe> allRecipes = new ArrayList<>();
            User user = userRepository.findUserByName("test");
            if(type == 1){ //favourite recipes
                allRecipes = user.getUserFavouriteRecipes();
            }
            if(type == 2) { //user recipes
                allRecipes = user.getUserRecipes();
            }
            if(type == 3) { //preferred ingredients
                List<Ingredient> preferredIngredients = user.getUserPreferedIngredients();
                if (preferredIngredients != null && !preferredIngredients.isEmpty()) {
                    allRecipes = allRecipes.stream()
                            .filter(recipe -> recipe.getRecipeIngredients().stream()
                                    .anyMatch(ri -> preferredIngredients.contains(ri.getIngredient())))
                            .collect(Collectors.toList());
                }
            }
            else{
                allRecipes = recipeRepository.findAll();
            }
            if(hideAllergens && user.getUserAllergenInredients() != null
                    && !user.getUserAllergenInredients().isEmpty())
            {
                List<Ingredient> userAllergens = user.getUserAllergenInredients();
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
                            byte[] imageData = ImageUtilService
                                    .decompressImage(recipe.getRecipeImageData());
                            String base64ImageData = Base64.getEncoder().encodeToString(imageData);
                            recipeData.put("imageData", base64ImageData);
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
    public ResponseEntity<?> addToFavourites(Integer recipeId) {
        try {
            User user = userRepository.findUserByName("test");
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
    public ResponseEntity<?> deleteFromFavourites(Integer recipeId) {
        try {
            User user = userRepository.findUserByName("test");
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

    public  ResponseEntity<?> getFavouriteRecipes() {
        try {
            User user = userRepository.findUserByName("test");
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
    public  ResponseEntity<?> getUserRecipes() {
        try {
            User user = userRepository.findUserByName("test");
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
    public ResponseEntity<?> updateUserRecipe(Map<String, Object> recipeUpdate) {
        try {
            User user = userRepository.findUserByName("test");
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
    public ResponseEntity<?> deleteUserRecipe(Integer recipeId) {
        try {
            User user = userRepository.findUserByName("test");
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            if (recipe != null) {
                List<Recipe> userRecipes = user.getUserRecipes();
                if (userRecipes.contains(recipe)) {
                    userRecipes.remove(recipe);
                    deleteRecipe(recipeId);
                    userRepository.save(user);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Recipe updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating recipe");
        }
    }
}



