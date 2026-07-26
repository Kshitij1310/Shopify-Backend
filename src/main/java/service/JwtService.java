package service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtTokenProvider tokenProvider;

    public JwtService(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String generateToken(String subject, String userId) {
        return tokenProvider.createToken(subject, userId);
    }

    public Claims parseToken(String token) throws JwtException {
        return tokenProvider.parseClaims(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            parseToken(token);
            return false;
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }
}
