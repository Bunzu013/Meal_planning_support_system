package project.mealPlan.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.mealPlan.entity.Ingredient;
import project.mealPlan.entity.Recipe_Ingredient;
import project.mealPlan.repository.IngredientRepository;
import project.mealPlan.repository.Recipe_IngredientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private Recipe_IngredientRepository recipe_IngredientRepository;

    @Test
    void testDeleteIngredient_success() {
        Integer ingredientId = 1;
        Map<String, Object> ingredientInput = new HashMap<>();
        ingredientInput.put("ingredientId", ingredientId);
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientId);
        when(ingredientRepository.findByIngredientId(ingredientId)).thenReturn(ingredient);
        List<Recipe_Ingredient> recipes = new ArrayList<>();
        when(recipe_IngredientRepository.findByIngredient(ingredient)).thenReturn(recipes);
        ResponseEntity<?> responseEntity = ingredientService.deleteIngredient(ingredientInput);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("ingredient deleted", responseEntity.getBody());
        verify(ingredientRepository, times(1)).delete(ingredient);
        for (Recipe_Ingredient recipe : recipes) {
            assertEquals("deleted", recipe.getIngredient().getIngredientName());
            assertNull(recipe.getUnit());
            assertNull(recipe.getQuantity());
        }
    }

    @Test
    void testDeleteIngredient_noIngredientFound() {
        Integer ingredientId = 1;
        Map<String, Object> ingredientInput = new HashMap<>();
        ingredientInput.put("ingredientId", ingredientId);
        when(ingredientRepository.findByIngredientId(ingredientId)).thenReturn(null);
        ResponseEntity<?> responseEntity = ingredientService.deleteIngredient(ingredientInput);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("No ingredient found", responseEntity.getBody());
        verify(ingredientRepository, never()).delete(any());
    }

}
