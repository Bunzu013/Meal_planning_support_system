package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.WeekDay;

@Repository
public interface WeekDayRepository extends JpaRepository<WeekDay, Integer> {


    WeekDay findByWeekDayId(Integer weekDayID);
}
