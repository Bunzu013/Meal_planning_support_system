package project.mealPlan.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "mealPlans")
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mealPlanId;

    @Column(nullable = true)
    private Boolean shoppingListStatus = false;

    @OneToOne(mappedBy = "mealPlan", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "mealPlan")
    List<MealPlan_Meal> mealPlanMeals;
}
