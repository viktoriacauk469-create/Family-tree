package family_tree.service;

import family_tree.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO register();
    List<UserDTO> findAll();
}
