package project.mealPlan.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    @NotNull(message = "Username is required")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Surname is required")
    private String surname;

    @Column(nullable = false, unique = true, length = 45)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    @Size(min = 9,max =  9, message = "Phone number must be 9 characters long")
    private Integer phoneNumber;


    @Column(nullable = false)
    @NotBlank(message = "Phone prefix is required")
    @Size(max =  3, message = "Password must be max 3 characters long")
    private Integer phonePrefix;

    @Column
    private Boolean hiddenCalories;

    @Column
    private Timestamp blockedTo;

    @Column
    private String activationLink;

    @Column
    private Timestamp activationLinkValidityTime;

    @Column
    private String resetPasswordLink;

    @Column
    private Timestamp resetPasswordValidityTime;

    @ManyToMany
    private List<Ingredient> userPreferredIngredients;

    @ManyToMany
    private List<Ingredient> userAllergenIngredients;

    @ManyToMany
    private List<Recipe> userFavouriteRecipes;

    @OneToMany(mappedBy = "user")
    private List<Recipe> userRecipes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mealPlanId")
    private MealPlan mealPlan;

    @ManyToMany
    private List<UserRole> roles;

    public User(String name, String surname, String email, String password, Integer phoneNumber, Integer phonePrefix) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.phonePrefix = phonePrefix;
        this.mealPlan = new MealPlan();
    }
    public String getRolesAsString() {
        if (roles != null && !roles.isEmpty()) {
            StringBuilder rolesString = new StringBuilder();
            for (UserRole role : roles) {
                rolesString.append(role.getAuthority());
            }
            // Remove the trailing comma and space
            return rolesString.substring(0, rolesString.length() - 2);
        }
        return "No roles";
    }
}
