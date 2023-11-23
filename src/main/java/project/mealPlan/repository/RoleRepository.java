package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.UserRole; // Poprawny import

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByAuthority(String authority);

    UserRole findUserRoleByAuthority(String roleAdmin);

    UserRole findUserRoleById(long l);
}