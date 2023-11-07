package project.mealPlan.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
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
    ROLE_EDITOR: Rola edytora, która ma uprawnienia do edycji treści na stronie.
    ROLE_MANAGER: Rola menedżera, która może zarządzać innymi użytkownikami.
    ROLE_GUEST: Rola dla niezalogowanych użytkowników, którzy mają ograniczony dostęp do niektórych zasobów.*/