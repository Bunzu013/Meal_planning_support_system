package project.mealPlan.configuration;

import project.mealPlan.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getName(), user.getSurname()))
                .setIssuer("CodeJava")
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

}


