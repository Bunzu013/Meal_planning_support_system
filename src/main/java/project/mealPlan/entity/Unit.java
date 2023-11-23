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
@Table(name = "units")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer unitId;

    @Column(nullable = false)
    @NotNull(message = "Unit name is required")
    private String unitName;

    @OneToMany(mappedBy = "unit")
    List<Recipe_Ingredient> unitsInRecipe;

    public Unit(String name) {
        this.unitName = name;
    }

}
