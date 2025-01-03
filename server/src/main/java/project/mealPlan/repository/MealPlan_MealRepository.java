package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mealPlan.entity.Meal;
import project.mealPlan.entity.MealPlan;
import project.mealPlan.entity.MealPlan_Meal;
import project.mealPlan.entity.WeekDay;

import java.util.List;

public interface MealPlan_MealRepository extends JpaRepository<MealPlan_Meal, Integer> {
    MealPlan_Meal findByMealAndWeekDayAndMealPlan(Meal meal, WeekDay weekDay, MealPlan mealPlan);

    void deleteByMeal(Meal meal);

    MealPlan_Meal findByMealAndMealPlan(Meal meal, MealPlan mealPlan);

    List<MealPlan_Meal> findByMeal(Meal meal);
}
