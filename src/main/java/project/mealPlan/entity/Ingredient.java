package project.mealPlan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;

    @Column(nullable = false)
    @NotNull(message = "Ingredient name is required")
    private String ingredientName;

    @Column
    private Boolean commonAllergen = false;

    @ManyToMany(mappedBy = "userPreferredIngredients")
    List<User> preferredByUser;

    @ManyToMany(mappedBy = "userAllergenIngredients")
    List<User> userAllergen;

    @OneToMany(mappedBy = "ingredient")
    List<Recipe_Ingredient> ingredientInRecipes;

    public Ingredient(String name) {
        this.ingredientName = name;
    }
}
