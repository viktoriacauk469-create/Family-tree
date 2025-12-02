package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ProfileRestController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDTO getProfile(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

    @PutMapping("/me")
    public String updateProfile(Principal principal,
                                @RequestBody UpdateProfileRequest request) {

        UserDTO user = userService.getUserByEmail(principal.getName());

        user.setFirstName(request.firstName);
        user.setLastName(request.lastName);
        user.setBloodType(request.bloodType);
        user.setRhesusFactor(request.rhesusFactor);
        user.setAge(request.age);
        user.setGender(user.getGender());

        userService.updateUser(user);

        return "Profile updated";
    }

    @Data
    static class UpdateProfileRequest {
        String firstName;
        String lastName;
        String bloodType;
        String rhesusFactor;
        String gender;
        Integer age;
    }
}
