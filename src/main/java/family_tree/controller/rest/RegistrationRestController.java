package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.service.UserService;
import family_tree.service.implementation.EmailService;
import family_tree.util.RandomNumberGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RegistrationRestController {

    private final UserService userService;
    private final EmailService emailService;

    // Register - accept JSON UserDTO
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email is already in use"));
            }

            RandomNumberGenerator.VerificationCode verificationCode = RandomNumberGenerator.generateCodeWithExpiry();
            userService.register(userDTO, verificationCode);

            // send verification code
            emailService.sendVerificationCodeEmail(userDTO.getEmail(), verificationCode.code());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Registration successful. Verification code sent to email.",
                    "email", userDTO.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed", "details", e.getMessage()));
        }
    }
}
