package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.*;
import project.mealPlan.repository.IngredientRepository;
import project.mealPlan.repository.Recipe_IngredientRepository;
import project.mealPlan.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientService {
    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    Recipe_IngredientRepository  recipe_IngredientRepository ;
    @Autowired
    UserService userService;

    public ResponseEntity<?> addToPreferred(Integer ingredientId, Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userPreferredIngredients = user.getUserPreferredIngredients();

                if (!userPreferredIngredients.contains(ingredient)) {
                    user.getUserPreferredIngredients().add(ingredient);
                    userRepository.save(user);
                    return new ResponseEntity<>
                            ("Ingredient added to preferred", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>
                            ("Ingredient is already in preferred list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?>  deleteFromPreferredIngredients(Integer ingredientId,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userPreferredIngredients = user.getUserPreferredIngredients();
                if (userPreferredIngredients.contains(ingredient)) {
                    user.getUserPreferredIngredients().remove(ingredient);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("Ingredient removed from preferred");
                } else {
                    return new ResponseEntity<>
                            ("This ingredient is not in the list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>
                    ("Error deleting from preferred ingredients",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public  ResponseEntity<?> getUserPreferredIngredients(Authentication authentication)
    {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            List<Map<String,Object>> ingredientInfoList = new ArrayList<>();
            List<Ingredient> preferredIngredients = user.getUserPreferredIngredients();
            for(Ingredient ingredient : preferredIngredients)
            {
                Map<String,Object> ingredientInfo = new HashMap<>();
                ingredientInfo.put("ingredientId", ingredient.getIngredientId());
                ingredientInfo.put("ingredientName", ingredient.getIngredientName());
                ingredientInfoList.add(ingredientInfo);
            }
            return ResponseEntity.ok(ingredientInfoList);
        } catch (Exception e) {
            return new ResponseEntity<>("Error receiving user preferred ingredients",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?>  getAllIngredients()
    {
        try {
           List<Map<String,Object>> ingredientInfoList = new ArrayList<>();
            List<Ingredient> ingredients = ingredientRepository.findAll();
            for(Ingredient ingredient : ingredients)
            {
                Map<String,Object> ingredientInfo = new HashMap<>();
                ingredientInfo.put("ingredientId", ingredient.getIngredientId());
                ingredientInfo.put("ingredientName", ingredient.getIngredientName());
                ingredientInfoList.add(ingredientInfo);
            }
            return new ResponseEntity<>(ingredientInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error receiving ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addToAllergens(Integer ingredientId,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userAllergens = user.getUserAllergenIngredients();

                if (!userAllergens.contains(ingredient)) {
                    user.getUserAllergenIngredients().add(ingredient);
                    userRepository.save(user);
                    return new ResponseEntity<>
                            ("Ingredient added to allergens", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>
                            ("Ingredient is already in allergens list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?>  deleteFromUserAllergens(Integer ingredientId,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userAllergens = user.getUserAllergenIngredients();
                if (userAllergens.contains(ingredient)) {
                    user.getUserAllergenIngredients().remove(ingredient);
                    userRepository.save(user);
                    return new ResponseEntity<>
                            ("Ingredient removed from allergens", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>
                            ("This ingredient is not in the list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>
                    ("Error deleting from user allergen ingredients",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public  ResponseEntity<?> getUserAllergens(Authentication authentication)
    {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = userService.findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            List<Map<String,Object>> ingredientInfoList = new ArrayList<>();
            List<Ingredient> allergenIngredients = user.getUserAllergenIngredients();
            for(Ingredient ingredient : allergenIngredients)
            {
                Map<String,Object> ingredientInfo = new HashMap<>();
                ingredientInfo.put("ingredientId", ingredient.getIngredientId());
                ingredientInfo.put("ingredientName", ingredient.getIngredientName());
                ingredientInfoList.add(ingredientInfo);
            }
            return ResponseEntity.ok(ingredientInfoList);
        } catch (Exception e) {
            return new ResponseEntity<>("Error receiving user allergen ingredients list",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addNewIngredient(Map<String, Object> ingredientInput)
    {
        try {
            String ingredient = (String) ingredientInput.get("ingredientName");
            if(ingredientRepository.findByIngredientName(ingredient) == null){
                Ingredient newIngredient = new Ingredient(ingredient);
                ingredientRepository.save(newIngredient);
                return new ResponseEntity<>("New ingredient added", HttpStatus.OK);
            }
            return new ResponseEntity<>("Ingredient already exists", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> updateIngredient(Map<String, Object> ingredientInput)
    {
        try {
            Integer ingredientId = (Integer) ingredientInput.get("ingredientId");
            String ingredientName = (String) ingredientInput.get("ingredientName");
            if(ingredientRepository.findByIngredientName(ingredientName) == null) {
                if (ingredientId != null) {
                    Ingredient ingredientUpdate = ingredientRepository.findByIngredientId(ingredientId);
                    if (ingredientUpdate != null) {
                        ingredientUpdate.setIngredientName(ingredientName);
                        ingredientRepository.save(ingredientUpdate);
                        return new ResponseEntity<>("Ingredient updated", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("No ingredient found", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }else{
                return new ResponseEntity<>("Ingredient with this name already exists", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Error updating ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> deleteIngredient(Map<String, Object> ingredientInput)
    {
        try {
            Integer ingredientId = (Integer) ingredientInput.get("ingredientId");
            if (ingredientId != null) {
                Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
                if (ingredient != null) {
                    List<Recipe_Ingredient> recipes = recipe_IngredientRepository.findByIngredient(ingredient) ;
                    Ingredient ingredientTemp = ingredientRepository.findByIngredientName("deleted");
                    for (Recipe_Ingredient recipe : recipes) {
                        recipe.setIngredient(ingredientTemp);
                        recipe.setUnit(null);
                        recipe.setQuantity(null);
                    }
                    ingredientRepository.delete(ingredient);
                    return new ResponseEntity<>("ingredient deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No ingredient found", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>("Error deleting ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

