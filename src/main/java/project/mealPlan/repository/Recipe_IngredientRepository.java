package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.Recipe_Ingredient;
@Repository
public interface Recipe_IngredientRepository extends JpaRepository<Recipe_Ingredient, Integer> {
}
