package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.model.PersonalInformation;
import family_tree.service.PersonalService;
import family_tree.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/relatives")
public class PersonalInformationRestController {

    private final PersonalService personalService;
    private final UserService userService;

    @GetMapping
    public List<PersonalInformation> getRelatives(Principal principal) {
        UserDTO user = userService.getUserByEmail(principal.getName());
        return personalService.getPersonalsForUser(user.getId());
    }

    @PostMapping
    public PersonalInformation addRelative(Principal principal,
                                           @RequestBody RelativeRequest request) {
        UserDTO user = userService.getUserByEmail(principal.getName());
        PersonalInformation person = PersonalInformation.builder()
                .firstName(request.firstName)
                .lastName(request.lastName)
                .age(request.age)
                .build();
        return personalService.createPersonalForUser(user.getId(), person);
    }

    @DeleteMapping("/{id}")
    public String removeRelative(@PathVariable Long id) {
        personalService.removeRelative(id);
        return "Relative removed";
    }

    @Data
    static class RelativeRequest {
        String firstName;
        String lastName;
        Integer age;
    }
}
