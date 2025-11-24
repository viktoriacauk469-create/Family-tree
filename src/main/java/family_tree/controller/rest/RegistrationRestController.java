package family_tree.controller.rest;

import family_tree.dto.UserDTO;
import family_tree.service.UserService;
import family_tree.service.implementation.EmailService;
import family_tree.util.RandomNumberGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RegistrationRestController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/register")
    public Object register(@Valid @RequestBody UserDTO userDTO,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "Invalid input";
        }

        if (userService.existsByEmail(userDTO.getEmail())) {
            return "Email already in use";
        }

        RandomNumberGenerator.VerificationCode code = RandomNumberGenerator.generateCodeWithExpiry();
        userService.register(userDTO, code);
        emailService.sendVerificationCodeEmail(userDTO.getEmail(), code.code());

        return "Verification code sent to email";
    }
}
