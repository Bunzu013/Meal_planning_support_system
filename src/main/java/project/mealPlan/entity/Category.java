package project.mealPlan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeCategoryId;

    @Column(nullable = false)
    @NotNull(message = "Category name is required")
    private String recipeCategoryName;

    @ManyToMany(mappedBy = "recipeCategories")
    private List<Recipe> recipesInCategory = new ArrayList<>();


    public Category(String name) {
        this.recipeCategoryName = name;
    }
}
