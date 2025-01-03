package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mealPlan.entity.InitializationStatus;

public interface InitializationStatusRepository extends JpaRepository<InitializationStatus, Long> {
}