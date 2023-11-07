package project.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.mealPlan.entity.Recipe;
import project.mealPlan.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(Integer phoneNumber);
    User findUserByName(String name);
    User findUserByEmail(String email);
    User findUserByPhoneNumber(Integer phoneNumber);

    User findUserByUserId(Integer userId);
    List<User> findAllByUserFavouriteRecipesContains(Recipe recipe);

    User findByResetPasswordLink(String token);
}


