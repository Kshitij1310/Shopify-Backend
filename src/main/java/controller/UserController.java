package controller;

import model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.UserProfileRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserProfileRepository userProfileRepository;

    public UserController(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @GetMapping("/{uid}")
    public ResponseEntity<?> getByUid(@PathVariable String uid) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Rejected user lookup for uid={} because authentication was missing", uid);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authentication required"));
            }

            return userProfileRepository.findById(uid)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found")));
        } catch (Exception ex) {
            log.error("Failed to fetch user profile for uid={}", uid, ex);
            String message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Internal server error" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
        }
    }

    @PutMapping("/{uid}")
    public ResponseEntity<?> updateByUid(@PathVariable String uid, @RequestBody UserProfile updates) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Rejected user update for uid={} because authentication was missing", uid);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authentication required"));
            }

            return userProfileRepository.findById(uid)
                .map(existing -> {
                    if (updates.getEmail() != null) {
                        existing.setEmail(updates.getEmail());
                    }
                    if (updates.getRole() != null) {
                        existing.setRole(updates.getRole());
                    }
                    if (updates.getWalletBalance() != null) {
                        existing.setWalletBalance(updates.getWalletBalance());
                    }
                    if (updates.getDisplayName() != null) {
                        existing.setDisplayName(updates.getDisplayName());
                    }
                    if (updates.getPhone() != null) {
                        existing.setPhone(updates.getPhone());
                    }
                    if (updates.getPhotoURL() != null) {
                        existing.setPhotoURL(updates.getPhotoURL());
                    }
                    return userProfileRepository.save(existing);
                })
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found")));
        } catch (Exception ex) {
            log.error("Failed to update user profile for uid={}", uid, ex);
            String message = ex.getMessage() == null || ex.getMessage().isBlank() ? "Internal server error" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
        }
    }
}