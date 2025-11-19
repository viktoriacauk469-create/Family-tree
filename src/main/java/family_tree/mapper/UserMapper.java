package family_tree.mapper;

import family_tree.model.User;
import family_tree.dto.UserDTO;
import family_tree.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final EnumMapper enumMapper;

    public User toUser(UserDTO userDTO) {
        if (userDTO == null) return null;

        User user = User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(enumMapper.stringToRole(userDTO.getRole()))
                .bloodType(enumMapper.stringToBloodType(userDTO.getBloodType()))
                .rhesusFactor(enumMapper.stringToRhesusFactor(userDTO.getRhesusFactor()))
                .build();

        return user;
    }

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(String.valueOf(user.getRole()))
                .bloodType(enumMapper.bloodTypeToString(user.getBloodType()))
                .rhesusFactor(enumMapper.rhesusFactorToString(user.getRhesusFactor()))
                .build();

        return userDTO;
    }
}
