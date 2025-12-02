package family_tree.controller.rest;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return new AuthResponse("Login endpoint (implement auth later)");
    }

    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }

    record AuthResponse(String message) {}
}
