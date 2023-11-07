package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.Meal;
import project.mealPlan.entity.MealPlan;
import project.mealPlan.entity.MealPlan_Meal;
import project.mealPlan.entity.WeekDay;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {
    Meal findByMealId(Integer mealId);


}
