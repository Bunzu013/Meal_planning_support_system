package project.mealPlan.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = true)
    private Boolean hiddenCalories;

    @Column(nullable = true)
    private Timestamp blockedTo;

    @Column(nullable = true)
    private String activationLink;

    @Column(nullable = true)
    private Timestamp activationLinkValidityTime;

    @Column(nullable = true)
    private String resetPasswordLink;

    @Column(nullable = true)
    private Timestamp resetPasswordValidityTime;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Ingredient> userPreferedIngredients;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Ingredient> userAllergenInredients;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Recipe> userFavouriteRecipes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Recipe> userRecipes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mealPlanId")
    private MealPlan mealPlan;

   @ElementCollection(targetClass = UserRole.class)
 //   @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
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

    public void addPreferedIngredient(Ingredient ingredient) {
        this.userPreferedIngredients.add(ingredient);

    }
}
