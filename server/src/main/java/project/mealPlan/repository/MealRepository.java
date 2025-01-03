package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.*;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {
    Meal findByMealId(Integer mealId);

    List<Meal> findByMealRecipes(Recipe recipe);
}
