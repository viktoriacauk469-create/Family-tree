package family_tree.controller;

import family_tree.dto.UserDTO;
import family_tree.model.enums.BloodType;
import family_tree.model.enums.RhesusFactor;
import family_tree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/user/profile")
    public String showProfile(Model model, Principal principal) {
        if (resolveCurrentUser(principal) == null) {
            return "redirect:/login";
        }

        UserDTO user = resolveCurrentUser(principal);
        if (user == null) {
            model.addAttribute("errorMessage", "Користувача не знайдено.");
            return "user/profile";
        }

        // enum values for selects
        List<BloodType> bloodTypes = Arrays.asList(BloodType.values());
        List<RhesusFactor> rhesusOptions = Arrays.asList(RhesusFactor.values());

        model.addAttribute("user", user);
        model.addAttribute("bloodTypes", bloodTypes);
        model.addAttribute("rhesusOptions", rhesusOptions);

        return "user/profile";
    }

    @PostMapping("/user/profile")
    public String saveProfile(Principal principal,
                              @RequestParam("bloodType") String bloodType,
                              @RequestParam("rhFactor") String rhesusFactor,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (resolveCurrentUser(principal) == null) {
            return "redirect:/login";
        }

        UserDTO userDTO = resolveCurrentUser(principal);

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Користувача не знайдено.");
            return "redirect:/user/profile";
        }

        userDTO.setBloodType(bloodType);
        userDTO.setRhesusFactor(rhesusFactor);
        try {
            userService.updateUser(userDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Профіль збережено.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка при збереженні профілю.");
        }

        return "redirect:/user/profile";
    }


    private UserDTO resolveCurrentUser(Principal principal) {
        if (principal == null) return null;
        try {
            return userService.getUserByEmail(principal.getName());
        } catch (Exception e) {
            return null;
        }
    }
}
