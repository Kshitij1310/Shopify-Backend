package security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CustomUserDetailsService;
import service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var claims = jwtService.parseToken(token);
                String email = claims.getSubject();
                log.debug("JWT parsed for uri={} subject={}", request.getRequestURI(), email);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("SecurityContext authenticated for email={}", email);
                } else if (email == null) {
                    log.warn("JWT parsed without subject for uri={}", request.getRequestURI());
                }
            } catch (ExpiredJwtException ex) {
                log.warn("Expired JWT for uri={}: {}", request.getRequestURI(), ex.getMessage());
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("Expired JWT token", ex);
            } catch (JwtException | IllegalArgumentException ex) {
                log.warn("Invalid JWT for uri={}: {}", request.getRequestURI(), ex.getMessage());
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("Invalid JWT token", ex);
            }
        }
        filterChain.doFilter(request, response);
    }
}
