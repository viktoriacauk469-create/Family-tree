package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.model.enums.BloodType;
import family_tree.model.enums.RhesusFactor;
import family_tree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ProfileRestController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> showProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        try {
            UserDTO user = userService.getUserByEmail(principal.getName());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }
            List<BloodType> bloodTypes = Arrays.asList(BloodType.values());
            List<RhesusFactor> rhesusOptions = Arrays.asList(RhesusFactor.values());

            return ResponseEntity.ok(Map.of(
                    "user", user,
                    "bloodTypes", bloodTypes,
                    "rhesusOptions", rhesusOptions
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update profile: accept JSON { "bloodType": "...", "rhesusFactor": "..." }
    @PostMapping("/profile")
    public ResponseEntity<?> saveProfile(Principal principal,
                                         @RequestBody Map<String, String> payload) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        try {
            UserDTO userDTO = userService.getUserByEmail(principal.getName());
            if (userDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }

            String bloodType = payload.get("bloodType");
            String rhFactor = payload.get("rhesusFactor");

            userDTO.setBloodType(bloodType);
            userDTO.setRhesusFactor(rhFactor);

            userService.updateUser(userDTO);

            return ResponseEntity.ok(Map.of("message", "Profile updated", "user", userDTO));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error while saving profile", "details", ex.getMessage()));
        }
    }
}
