package project.mealPlan.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByRecipeCategoryName(String recipeCategoryName);
        Category findByRecipeCategoryId(Integer categoryId);
}

