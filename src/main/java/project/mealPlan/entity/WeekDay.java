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
@Table(name = "weekDays")
public class WeekDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weekDayId;

    @Column(nullable = false)
    @NotNull(message = "Week name is required")
    private String weekDayName;

    @OneToMany(mappedBy = "weekDay")
    List<MealPlan_Meal> mealsInWeekDay;
    public WeekDay(String name) {
        this.weekDayName = name;
    }
}
