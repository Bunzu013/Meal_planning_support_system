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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import project.mealPlan.repository.MealRepository;
import project.mealPlan.repository.RecipeRepository;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
class MealServiceTest {
    @InjectMocks
    private MealService mealService;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Test
    void testAddRecipeToMeal_success() {
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("recipeId", 1);
        recipeData.put("mealId", 2);
        Meal meal = new Meal();
        Recipe recipe = new Recipe();
        when(mealRepository.findByMealId(2)).thenReturn(meal);
        when(recipeRepository.findByRecipeId(1)).thenReturn(recipe);
        ResponseEntity<?> responseEntity = mealService.addRecipeToMeal(recipeData);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Recipe added to meal", responseEntity.getBody());
        verify(mealRepository, times(1)).findByMealId(2);
        verify(recipeRepository, times(1)).findByRecipeId(1);
        verify(mealRepository, times(1)).save(meal);
    }
}
