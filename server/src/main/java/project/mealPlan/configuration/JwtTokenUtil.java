package project.mealPlan.configuration;
import project.mealPlan.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import project.mealPlan.entity.UserRole;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Value("${app.jwt.secret:abcdefg}")
    private String SECRET_KEY;
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }
    public String generateJwtToken(User user) {
        List<String> roleNames = user.getRoles()
                .stream()
                .map(UserRole::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(String.format("%s,%s",
                        user.getName(),
                        user.getSurname()))
                .setIssuer("CodeJava")
                .claim("email", user.getEmail())
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()
                        + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    public String getSecretKey() {
        return SECRET_KEY;
    }
}
