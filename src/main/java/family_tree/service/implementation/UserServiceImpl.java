package family_tree.service.implementation;

import family_tree.dto.UserDTO;
import family_tree.mapper.UserMapper;
import family_tree.repository.UserRepository;
import family_tree.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;


    @Override
    public UserDTO register() {
        return null;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().iterator().ma
    }
}
