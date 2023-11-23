package project.mealPlan.configuration;

// JwtAuthenticationFilter.java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtTokenUtil.getSecretKey())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String email = String.valueOf(claims.get("email"));
            if (email != null) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                Object rolesObject = claims.get("roles");
                if (rolesObject instanceof List<?>) {
                    List<?> rolesList = (List<?>) rolesObject;
                    for (Object role : rolesList) {
                        if (role instanceof String) {
                            authorities.add(new SimpleGrantedAuthority((String) role));
                        }
                    }
                }
                return new UsernamePasswordAuthenticationToken(email, null, authorities);
            }
        }
        return null;
    }
}