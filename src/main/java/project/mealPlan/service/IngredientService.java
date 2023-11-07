package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import project.mealPlan.entity.Ingredient;
import project.mealPlan.entity.Recipe;
import project.mealPlan.entity.User;
import project.mealPlan.repository.IngredientRepository;
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

    public ResponseEntity<?> addToPreferred(Integer ingredientId) {
        try {
            User user = userRepository.findUserByName("test");
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userPreferredIngredients = user.getUserPreferedIngredients();

                if (!userPreferredIngredients.contains(ingredient)) {
                    user.addPreferedIngredient(ingredient);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("Ingredient added to preferred");
                } else {
                    return new ResponseEntity<>("Ingredient is already in preferred list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?>  deleteFromPreferredIngredients(Integer ingredientId) {
        try {
            User user = userRepository.findUserByName("test");
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userPreferredIngredients = user.getUserPreferedIngredients();
                if (userPreferredIngredients.contains(ingredient)) {
                    user.getUserPreferedIngredients().remove(ingredient);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("Ingredient removed from preferred");
                } else {
                    return new ResponseEntity<>("This ingredient is not in the list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting from preferred ingredients",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public  ResponseEntity<?> getUserPreferredIngredients()
    {
        try {
            User user = userRepository.findUserByName("test");
            List<Map<String,Object>> ingredientInfoList = new ArrayList<>();
            List<Ingredient> preferredIngredients = user.getUserPreferedIngredients();
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
                ingredientInfo.put("ingredientID", ingredient.getIngredientId());
                ingredientInfo.put("ingredientName", ingredient.getIngredientName());
                ingredientInfoList.add(ingredientInfo);
            }
            return new ResponseEntity<>(ingredientInfoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error receiving ingredients", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addToAllergens(Integer ingredientId) {
        try {
            User user = userRepository.findUserByName("test");
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userAllergens = user.getUserAllergenInredients();

                if (!userAllergens.contains(ingredient)) {
                    user.getUserAllergenInredients().add(ingredient);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("Ingredient added to allergens");
                } else {
                    return new ResponseEntity<>("Ingredient is already in allergens list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?>  deleteFromUserAllergens(Integer ingredientId) {
        try {
            User user = userRepository.findUserByName("test");
            Ingredient ingredient = ingredientRepository.findByIngredientId(ingredientId);
            if (ingredient != null) {
                List<Ingredient> userAllergens = user.getUserAllergenInredients();
                if (userAllergens.contains(ingredient)) {
                    user.getUserAllergenInredients().remove(ingredient);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK).body("Ingredient removed from allergens");
                } else {
                    return new ResponseEntity<>("This ingredient is not in the list", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Ingredient not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting from user allergen ingredients",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public  ResponseEntity<?> getUserAllergens()
    {
        try {
            User user = userRepository.findUserByName("test");
            List<Map<String,Object>> ingredientInfoList = new ArrayList<>();
            List<Ingredient> allergenIngredients = user.getUserAllergenInredients();
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
}

