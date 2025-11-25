package family_tree.controller;

import family_tree.dto.UserDTO;
import family_tree.model.PersonalInformation;
import family_tree.service.PersonalService;
import family_tree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/relatives")
public class PersonalInformationController {

    private final PersonalService personalService;
    private final UserService userService;

    @GetMapping
    public String getRelatives(Principal principal, Model model) {
        UserDTO user = userService.getUserByEmail(principal.getName());
        if (user == null) return "redirect:/login";

        List<PersonalInformation> personals = personalService.getPersonalsForUser(user.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("userPersonals", personals); // саме ця змінна використовується у Thymeleaf

        return "dashboard/relatives";
    }

    @PostMapping("/add")
    public String addRelative(Principal principal,
                              @RequestParam("relativeFirstName") String firstName,
                              @RequestParam("relativeLastName") String lastName,
                              @RequestParam("relativeAge") Integer age,
                              RedirectAttributes redirectAttributes) {
        try {
            UserDTO user = userService.getUserByEmail(principal.getName());
            PersonalInformation personal = PersonalInformation.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .age(age)
                    .build();
            personalService.createPersonalForUser(user.getId(), personal);
            return "redirect:/relatives";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/relatives";
        }
    }

    @PostMapping("/remove")
    public String removeRelative(@RequestParam Long personId,
                                 RedirectAttributes redirectAttributes) {
        try {
            personalService.removeRelative(personId); // тепер цей метод є
            return "redirect:/relatives";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/relatives";
        }
    }

}
