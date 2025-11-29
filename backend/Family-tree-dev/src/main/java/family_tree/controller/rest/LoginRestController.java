package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> loginRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            if (email == null || password == null || email.isBlank() || password.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Email and password are required"));
            }

            // Check if user exists
            if (!userService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid email or password"));
            }

            // Authenticate user using Spring Security
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );
                
                // Ensure session is created
                request.getSession(true);
                
                // Set authentication in security context
                org.springframework.security.core.context.SecurityContext securityContext = 
                        SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                
                // Save authentication to session for persistence across requests
                org.springframework.security.web.context.HttpSessionSecurityContextRepository securityContextRepository = 
                        new org.springframework.security.web.context.HttpSessionSecurityContextRepository();
                securityContextRepository.saveContext(
                        securityContext,
                        request,
                        response
                );

                // Get user info
                var userDTO = userService.getUserByEmail(email);

                return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "user", Map.of(
                                "id", userDTO.getId(),
                                "email", userDTO.getEmail(),
                                "bloodType", userDTO.getBloodType() != null ? userDTO.getBloodType() : "",
                                "rhesusFactor", userDTO.getRhesusFactor() != null ? userDTO.getRhesusFactor() : ""
                        )
                ));
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed", "details", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return ResponseEntity.ok(Map.of("message", "Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Logout failed", "details", e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("authenticated", false));
            }

            var userDTO = userService.getUserByEmail(auth.getName());
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", Map.of(
                            "id", userDTO.getId(),
                            "email", userDTO.getEmail(),
                            "bloodType", userDTO.getBloodType() != null ? userDTO.getBloodType() : "",
                            "rhesusFactor", userDTO.getRhesusFactor() != null ? userDTO.getRhesusFactor() : ""
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("authenticated", false, "error", e.getMessage()));
        }
    }
}
