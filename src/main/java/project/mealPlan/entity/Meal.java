package project.mealPlan.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mealId;

    @Column(nullable = true)
    private String description;

    @ManyToMany
    @JoinTable(name="Meal_Recipe",
            joinColumns = @JoinColumn(name="meal_id"),
            inverseJoinColumns = @JoinColumn(name="recipe_id"))
    private List<Recipe> mealRecipes = new ArrayList<>();

    @OneToMany(mappedBy = "meal")  // Dodaj odpowiednią relację do MealPlan_Meal
    private List<MealPlan_Meal> mealPlanMeals;
    public void addNewRecipe(Recipe recipe) {
        this.mealRecipes.add(recipe);
    }
}
