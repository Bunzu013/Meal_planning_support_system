package project.mealPlan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeId;

    @Column(nullable = false)
    @NotNull(message = "Recipe name is required")
    private String recipeName;

    @Column(nullable = false, length = 2000)
    private String notes;

    @Column()
    private Integer calories;

    @Lob
    @Column(name = "image_data",columnDefinition = "LONGBLOB")
    private byte[] recipeImageData;

    @Column(name = "image_name")
    private String recipeImageName;

    @ManyToMany(mappedBy = "userFavouriteRecipes")
    List<User> userFavouriteLists;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToMany
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> recipeCategories = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "recipe_filter",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "filter_id"))
    private List<Filter> recipeFilters = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipeId")
    private List<Recipe_Ingredient> recipeIngredients;

    @ManyToMany(mappedBy = "mealRecipes")
    private List<Meal> recipeInMeals = new ArrayList<>();

    public Recipe(String name)
    {
        this.recipeName = name;

    }

}
