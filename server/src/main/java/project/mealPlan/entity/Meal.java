package project.mealPlan.entity;

import lombok.*;

import javax.persistence.*;
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


    private String description;

    @ManyToMany
    @JoinTable(name="Meal_Recipe",
            joinColumns = @JoinColumn(name="meal_id"),
            inverseJoinColumns = @JoinColumn(name="recipe_id"))
    private List<Recipe> mealRecipes = new ArrayList<>();

    @OneToMany(mappedBy = "meal")
    private List<MealPlan_Meal> mealPlanMeals;

}
