package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.service.UserService;
import family_tree.service.implementation.EmailService;
import family_tree.util.RandomNumberGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RegistrationRestController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO,
                           BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid input", "details", bindingResult.getAllErrors().toString()));
            }

            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already in use"));
            }

            RandomNumberGenerator.VerificationCode code = RandomNumberGenerator.generateCodeWithExpiry();
            userService.register(userDTO, code);
            emailService.sendVerificationCodeEmail(userDTO.getEmail(), code.code());

            return ResponseEntity.ok(Map.of("message", "Verification code sent to email"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed", "details", e.getMessage()));
        }
    }
}
