package project.mealPlan.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.mealPlan.configuration.JwtTokenUtil;
import project.mealPlan.entity.*;
import project.mealPlan.repository.*;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Calendar;
import org.springframework.security.core.Authentication;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    MealRepository mealRepository;
    @Autowired
    MealPlan_MealRepository mealPlan_mealRepository;
    @Autowired
    MealPlanRepository mealPlanRepository;
@Autowired
MealService mealService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,JwtTokenUtil jwtTokenUtil ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
        );
    }

    public ResponseEntity<?> findUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            String email = authentication.getName();
            User user = userRepository.findUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user");
        }
    }

    public ResponseEntity<?> addUser(User user) {
        try {
            boolean emailExists = userRepository.existsByEmail(user.getEmail());
            boolean phoneNumberExists = userRepository.existsByPhoneNumber(user.getPhoneNumber());
            if (emailExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("User with this email already exists");
            }
            if (phoneNumberExists) {
                User userTemp = userRepository.findUserByPhoneNumber(user.getPhoneNumber());
                if (userTemp.getPhonePrefix().equals(user.getPhonePrefix()))
                  return ResponseEntity.status(HttpStatus.CONFLICT)
                          .body("User with this phone number already exists");
            }
            user.setMealPlan(new MealPlan());
            UserRole defaultRole = roleRepository.findByAuthority("ROLE_USER");
            if (defaultRole == null) {
                defaultRole = new UserRole("ROLE_USER");
                roleRepository.save(defaultRole);
                user.setRoles(Collections.singletonList(defaultRole));
            }else {
                user.setRoles(Collections.singletonList(defaultRole));
            }
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user");
        }
    }
    public ResponseEntity<String> login(User user) {
        try {
            User existingUser = userRepository.findUserByEmail(user.getEmail());
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: user not found");
            }
            LocalDateTime currentTimestamp = LocalDateTime.now();
            Timestamp timestampResult = Timestamp.valueOf(currentTimestamp);
            Timestamp block = existingUser.getBlockedTo();
            if (block != null && block.after(timestampResult)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User blocked till: " + block);
            }
            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: incorrect password");
            }
            String token = jwtTokenUtil.generateJwtToken(existingUser);
            return ResponseEntity.ok().body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @Transactional
    public ResponseEntity<?> getUserData(Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userName", user.getName());
            userInfo.put("userSurname", user.getSurname());
            userInfo.put("email", user.getEmail());
            userInfo.put("phoneNumber", user.getPhoneNumber());
            userInfo.put("phonePrefix", user.getPhonePrefix());
            userInfo.put("hiddenCalories", user.getHiddenCalories());
           // userInfo.put("roles", user.getRoles());
            List<String> preferredIngredients = new ArrayList<>();
            for (Ingredient ingredient : user.getUserPreferredIngredients()) {
                preferredIngredients.add(ingredient.getIngredientName());
            }
            userInfo.put("userPreferredIngredients", preferredIngredients);

            List<String> favouriteRecipes = new ArrayList<>();
            for (Recipe recipe : user.getUserFavouriteRecipes()) {
                favouriteRecipes.add(recipe.getRecipeName());
            }
            userInfo.put("userFavouriteRecipes", favouriteRecipes);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting user data");
        }
    }

    public  ResponseEntity<?> editUserData(Map<String, Object> userInfo,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if (userInfo.get("userName") != null) {
                String userName = (String) userInfo.get("userName");
                user.setName(userName);
            }
            if (userInfo.get("userSurname") != null) {
                String surname = (String) userInfo.get("userSurname");
                user.setSurname(surname);
            }
            if (userInfo.get("phoneNumber") != null) {
                Integer phoneNumber = (Integer) userInfo.get("phoneNumber");
                user.setPhoneNumber(phoneNumber);
            }
            if (userInfo.get("phonePrefix") != null) {
                Integer phonePrefix = (Integer) userInfo.get("phonePrefix");
                user.setPhonePrefix(phonePrefix);
            }
            userRepository.save(user);
            return new ResponseEntity<>("User data updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user data");
        }
    }
    public ResponseEntity<String> editPassword(Map<String, Object> data,
                                               Authentication authentication) {
        try {
            if (data.get("oldPassword") != null) {
                String oldPassword = (String) data.get("oldPassword");
                User user = new User();
                ResponseEntity<?> responseEntity = findUser(authentication);
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    user = (User) responseEntity.getBody();
                }
                if (user == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("User not found");
                }
                if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Incorrect password");
                }
                if (data.get("newPassword") != null) {
                    String newPassword = (String) data.get("newPassword");
                    if (oldPassword.equals(newPassword)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("New password must be different");
                    }
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(hashedPassword);
                    userRepository.save(user);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Password changed successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("New password cannot be null");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password cannot be null");
            }
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<?> hideCalories(Boolean hide,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if (hide != null) {
                if (user.getHiddenCalories() == hide)
                    return new ResponseEntity<>
                            ("Calorie visibility is already set this way ", HttpStatus.OK);
                user.setHiddenCalories(hide);
                userRepository.save(user);
            }
            return new ResponseEntity<>
                    ("Calories visibility changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>
                    ("Error changing calories visibility ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateResetPasswordToken(String token, String email,Authentication authentication) {
        try {
            User user = new User();
            ResponseEntity<?> responseEntity = findUser(authentication);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                user = (User) responseEntity.getBody();
            }
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if (user != null) {
                user.setResetPasswordLink(token);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 10);
                Timestamp validityTime = new Timestamp(calendar.getTime().getTime());
                user.setResetPasswordValidityTime(validityTime);
                userRepository.save(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Token created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing calories visibility");
        }
    }


    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordLink(token);
    }

    public ResponseEntity<?> updatePassword(User user, String newPassword) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reset password link has expired.");
        }
        try {
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            user.setResetPasswordLink(null);
            user.setResetPasswordValidityTime(null);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
        }
    }
    @Transactional
    public ResponseEntity<?> deleteUser(Map<String, Object> userInput) {
        try {
            String email = (String) userInput.get("email");
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                List<Recipe> userRecipes = user.getUserRecipes();
                User admin = userRepository.findUserByName("ADMIN");
                for (Recipe recipe : userRecipes) {
                    recipe.setUser(admin);
                }
                MealPlan mealplan = user.getMealPlan();
                List<MealPlan_Meal> mealPlanMeals = mealplan.getMealPlanMeals();
                for (MealPlan_Meal mealPlanMeal : mealPlanMeals) {
                    mealPlanMeal.setWeekDay(null);
                    mealPlanMeal.setMealPlan(null);
                    Meal meal = mealPlanMeal.getMeal();
                    mealPlanMeal.setMeal(null);
                    mealService.deleteMeal(meal.getMealId());
                    mealPlan_mealRepository.delete(mealPlanMeal);
                }
                mealPlanRepository.delete(mealplan);
                user.getUserFavouriteRecipes().clear();
                user.getRoles().clear();
                user.getUserAllergenIngredients().clear();
                user.getUserPreferredIngredients().clear();
                userRepository.delete(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body("User deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
    @Transactional
    public ResponseEntity<?> blockUser(Map<String, Object> userInput) {
        try {
            String email = (String) userInput.get("email");
            User user = userRepository.findUserByEmail(email);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("user not found");
            }
            LocalDateTime currentTimestamp = LocalDateTime.now();
            LocalDateTime newTimestamp = currentTimestamp.plusMonths(2);
            Timestamp timestampResult = Timestamp.valueOf(newTimestamp);
            user.setBlockedTo(timestampResult);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("User blocked");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error blocking user");
        }
    }
    @Transactional
    public ResponseEntity<?> setAdmin() {
        try {
            User user = userRepository.findUserByEmail("admin@gmail.com");
            List<UserRole> existingRoles = user.getRoles();
            System.out.println("Existing Roles: " + existingRoles);

            UserRole roleAdmin = roleRepository.findUserRoleById(2);
            if (roleAdmin == null) {
                roleAdmin = new UserRole("ROLE_ADMIN");
                roleRepository.save(roleAdmin);
            }

            if (!existingRoles.contains(roleAdmin)) {
                user.getRoles().clear();
                user.getRoles().add(roleAdmin);
                userRepository.save(user);
            }

            List<UserRole> updatedRoles = user.getRoles();
            System.out.println("Updated Roles: " + updatedRoles);

            return ResponseEntity.status(HttpStatus.OK).body("Role added");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user roles");
        }
    }
}
