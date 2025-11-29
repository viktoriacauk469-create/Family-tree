package family_tree.controller.rest;

import family_tree.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class VerifyRestController {

    private final UserService userService;

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyRequest request) {
        try {
            String email = request.getEmail();
            String code = request.getCode();

            if (email == null || code == null || email.isBlank() || code.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Email and verification code are required"));
            }

            if (!userService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User with this email not found"));
            }

            boolean valid = userService.verifyUserByCode(email, code);
            if (!valid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid or expired verification code"));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Email verified successfully. You can now log in.",
                    "email", email
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Verification failed", "details", e.getMessage()));
        }
    }

    @Data
    static class VerifyRequest {
        private String email;
        private String code;
    }
}
