package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.MealPlan;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Integer> {
    MealPlan findByMealPlanId(Integer mealPlanId);


}
