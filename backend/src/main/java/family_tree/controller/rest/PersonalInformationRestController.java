package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.model.PersonalInformation;
import family_tree.service.PersonalService;
import family_tree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/relatives")
public class PersonalInformationRestController {

    private final PersonalService personalService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getRelatives(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }
        try {
            UserDTO user = userService.getUserByEmail(principal.getName());
            List<PersonalInformation> userPersonals = personalService.getRelativesForUser(user.getId());
            return ResponseEntity.ok(Map.of(
                    "userId", user.getId(),
                    "relatives", userPersonals
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Create relative - accepts JSON body for PersonalInformation (or a DTO with necessary fields)
    @PostMapping
    public ResponseEntity<?> addRelative(Principal principal,
                                         @RequestBody PersonalInformation personal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }
        try {
            UserDTO user = userService.getUserByEmail(principal.getName());
            personalService.createPersonalForUser(user.getId(), personal);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Relative added", "relative", personal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete by id
    @DeleteMapping("/{personId}")
    public ResponseEntity<?> removeRelative(@PathVariable Long personId) {
        try {
            personalService.removeRelative(personId);
            return ResponseEntity.ok(Map.of("message", "Relative removed", "personId", personId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
