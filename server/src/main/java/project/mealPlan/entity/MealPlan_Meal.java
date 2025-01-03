package project.mealPlan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mealplan_meal")
public class MealPlan_Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mealPlanMealId;

    @ManyToOne
    @JoinColumn(name="mealId", nullable=false)
    private Meal meal;

    @ManyToOne
    @JoinColumn(name="mealPlanId", nullable=false)
    private MealPlan mealPlan;

    @ManyToOne
    @JoinColumn(name="weekDayId", nullable=false)
    private WeekDay weekDay;

}
