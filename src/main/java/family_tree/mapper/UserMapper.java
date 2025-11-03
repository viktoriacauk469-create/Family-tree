package family_tree.mapper;

import family_tree.model.User;
import family_tree.dto.UserDTO;
import family_tree.model.enums.Role;

public class UserMapper {

    public User toUser(UserDTO userDTO) {

        if (userDTO == null) return null;

        User user = User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(Role.valueOf(userDTO.getRole()))
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
                .build();

        return userDTO;
    }
}
