package project.mealPlan.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;
    public UserRole(String authority) {
        this.authority = authority;
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
/*    ROLE_USER: Standardowa rola przyznawana wszystkim zarejestrowanym użytkownikom.
    ROLE_ADMIN: Rola administratora, która ma pełne uprawnienia w systemie.
    ROLE_GUEST: Rola dla niezalogowanych użytkowników, którzy mają ograniczony dostęp do niektórych zasobów.*/