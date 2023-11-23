package project.mealPlan.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.mealPlan.entity.Meal;
import project.mealPlan.entity.Recipe;
import project.mealPlan.repository.*;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private Recipe_IngredientRepository recipeIngredientRepository;
    @InjectMocks
    private RecipeService recipeService;

    @Test
    void deleteRecipe_success() {
        Recipe recipe = new Recipe();
        when(recipeRepository.findByRecipeId(any())).thenReturn(recipe);
        when(userRepository.findAllByUserFavouriteRecipesContains(any())).thenReturn(Collections.emptyList());
        when(mealRepository.findByMealRecipes(any())).thenReturn(Collections.singletonList(new Meal()));
        ResponseEntity<?> response = recipeService.deleteRecipe(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Recipe deleted", response.getBody());
        verify(recipeIngredientRepository, times(1)).deleteAll(any());
    }
    @Test
    void deleteRecipe_recipeNotFound() {
        when(recipeRepository.findByRecipeId(any())).thenReturn(null);
        ResponseEntity<?> response = recipeService.deleteRecipe(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recipe not found", response.getBody());
        verify(recipeIngredientRepository, never()).deleteAll(any());
    }
    @Test
    void updateRecipe_success() {
        when(recipeRepository.findByRecipeId(any())).thenReturn(new Recipe());
        ResponseEntity<?> response = recipeService.updateRecipe(Collections.singletonMap("recipeId", 1));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Recipe updated", response.getBody());
    }
}
