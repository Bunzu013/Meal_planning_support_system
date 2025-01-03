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
@Table(name = "filters")
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeFilterId;

    @Column(nullable = false)
    @NotNull(message = "Filter name is required")
    private String recipeFilterName;

    @ManyToMany(mappedBy = "recipeFilters")
    private List<Recipe> recipesInFilter = new ArrayList<>();

    public Filter(String name) {
        this.recipeFilterName = name;
    }
}
