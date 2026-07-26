package service;

import dto.AuthResponse;
import dto.LoginRequest;
import dto.RegisterRequest;
import model.UserProfile;
import repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserProfileRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserProfileRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        UserProfile user = new UserProfile();
        user.setUid(java.util.UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setDisplayName(request.getName());
        user.setRole(request.getRole() != null && !request.getRole().isBlank() ? request.getRole() : "customer");
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setWalletBalance(java.math.BigDecimal.ZERO);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getUid());
        return new AuthResponse(token, user.getUid());
    }

    public AuthResponse login(LoginRequest request) {
        Optional<UserProfile> opt = userRepository.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            log.warn("Login failed for email={}: invalid credentials", request.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        }
        UserProfile user = opt.get();
        String storedHash = user.getPasswordHash();
        boolean matches = false;
        if (storedHash == null) {
            matches = false;
        } else if (storedHash.startsWith("$2") || storedHash.startsWith("{bcrypt}")) {
            // stored as BCrypt
            matches = passwordEncoder.matches(request.getPassword(), storedHash);
        } else {
            // legacy plain-text or non-BCrypt hash from migration.
            if (storedHash.equals(request.getPassword())) {
                matches = true;
                try {
                    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                    userRepository.save(user);
                } catch (Exception ignore) {
                }
            }
        }

        if (!matches) {
            log.warn("Login failed for email={}: invalid credentials", request.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("Login successful for email={}", request.getEmail());
        String token = jwtService.generateToken(user.getEmail(), user.getUid());
        return new AuthResponse(token, user.getUid());
    }

    public void logout() {
        log.info("Logout called");
    }
}
