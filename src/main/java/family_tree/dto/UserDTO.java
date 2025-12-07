package family_tree.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password length must be between 6 and 100 characters")
    private String password;

    private String role;

    // ------- Personal Information ---------

    private String firstName;
    private String lastName;
    @Min(value = 0, message = "Age must be 0 or greater")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;
    private String gender;
    private String bloodType;
    private String rhesusFactor;
}
