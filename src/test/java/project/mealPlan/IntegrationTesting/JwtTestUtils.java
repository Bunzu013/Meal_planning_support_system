package project.mealPlan.IntegrationTesting;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.mealPlan.entity.User;
import project.mealPlan.entity.UserRole;

import java.util.Arrays;
import java.util.List;

public class JwtTestUtils {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User createSampleUser() {
        User user = new User();
        user.setName("test");
        user.setSurname("user");
        user.setEmail("test@gmail.com");
        String rawPassword = "123456789";
        String encodedPassword = encodePassword(rawPassword);
        user.setPassword(encodedPassword);
        List<UserRole> roles = getUserRoles();
        user.setRoles(roles);

        return user;
    }

    private static List<UserRole> getUserRoles() {
        UserRole roleUser = new UserRole("ROLE_USER");
        UserRole roleAdmin = new UserRole("ROLE_ADMIN");
        return Arrays.asList(roleUser, roleAdmin);
    }

    private static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
