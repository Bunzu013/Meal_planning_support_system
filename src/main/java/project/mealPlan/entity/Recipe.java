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

    @Column(nullable = true)
    private Integer calories;

    @Lob
    @Column(name = "image_data",nullable = true,columnDefinition = "LONGBLOB")
    private byte[] recipeImageData;

    @Column(name = "image_name", nullable = true)
    private String recipeImageName;

    @ManyToMany(mappedBy = "userFavouriteRecipes")
    List<User> userFavouriteLists;

    @ManyToOne
    @JoinColumn(name="userId", nullable=true)
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

    public void addCategory(Category category) {
        this.recipeCategories.add(category);
      //  category.getRecipesInCategory().add(this);
    }

    public void removeCategory(Category category) {
        this.recipeCategories.remove(category);
         category.getRecipesInCategory().remove(this);
    }

    public void addFilter(Filter filter) {
        this.recipeFilters.add(filter);
      //  filter.getRecipesInFilter().add(this);
    }

    public void removeFilter(Filter filter) {
        this.recipeFilters.remove(filter);
       // filter.getRecipesInFilter().remove(this);
    }
}
