package project.mealPlan.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.mealPlan.entity.User;
import project.mealPlan.entity.UserRole;
import project.mealPlan.repository.RoleRepository;
import project.mealPlan.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testAddUser_success() {
        User user = new User("John", "Doe", "john.doe@example.com", "password", 123456789, 123);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(user.getPhoneNumber())).thenReturn(false);
        when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(new UserRole("ROLE_USER"));
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        ResponseEntity<?> response = userService.addUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String responseBody = (String) response.getBody();
        assertEquals("User created successfully", responseBody);
    }
    @Test
    public void testLoginUser_notFound() {
        User user = new User("John", "Doe", "john.doe@example.com", "hashedPassword", 123456789, 123);
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(null);
        ResponseEntity<String> response = userService.login(user);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Login failed: user not found", response.getBody());
    }
    @Test
    public void testLogin_userBlocked() {
        User user = new User("John", "Doe", "john.doe@example.com", "hashedPassword", 123456789, 123);
        user.setBlockedTo(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(user);
        ResponseEntity<String> response = userService.login(user);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User blocked till: " + user.getBlockedTo(), response.getBody());
    }
    @Test
    public void testLogin_incorrectPassword() {
        User user = new User("John", "Doe", "john.doe@example.com", "hashedPassword", 123456789, 123);
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(user);
        User userWithIncorrectPassword = new User("John", "Doe", "john.doe@example.com", passwordEncoder.encode("wrongPassword"), 123456789, 123);
        ResponseEntity<String> response = userService.login(userWithIncorrectPassword);
        assertEquals("Login failed: incorrect password", response.getBody());
    }
}
