package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.Filter;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Integer> {
    Filter findByRecipeFilterName(String recipeFilterName);

    Filter findByRecipeFilterId(Integer filterId);
}


