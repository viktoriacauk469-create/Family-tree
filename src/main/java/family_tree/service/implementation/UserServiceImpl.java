package family_tree.service.implementation;

import family_tree.dto.UserDTO;
import family_tree.exception.UserNotFoundException;
import family_tree.logger.Logger;
import family_tree.mapper.UserMapper;
import family_tree.model.User;
import family_tree.model.UserVerification;
import family_tree.repository.UserRepository;
import family_tree.repository.UserVerificationRepository;
import family_tree.service.UserService;
import family_tree.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserVerificationRepository userVerificationRepository;
    private final Logger securityLogger;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO register(UserDTO userDTO, RandomNumberGenerator.VerificationCode verificationCode) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        UserVerification userVerification = UserVerification.builder()
                .user(savedUser) // <- важливо прив'язати користувача
                .verificationToken(verificationCode.code())
                .verificationTokenExpiry(verificationCode.expiry())
                .isVerified(false)
                .build();

        userVerificationRepository.save(userVerification);

        securityLogger.logRegistrationSuccess(savedUser.getEmail());

        return userMapper.toUserDTO(savedUser);
    }

    @Override
    public void deleteUser(String email) {
        if (email == null) {
            throw new UserNotFoundException("user.not_found");
        }
        userRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(String email, String newPassword) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user.not_found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        securityLogger.logPasswordResetSuccess(email);
        userRepository.save(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository
                .findUserByEmail(email)
                .map(userMapper::toUserDTO).orElseThrow(() -> new UserNotFoundException("User with email: " + email + "not found"));
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository
                .findUserById(id)
                .map(userMapper::toUserDTO);
    }

    @Override
    @Transactional
    public boolean verifyUserByCode(String email, String code) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user.not_found"));

        UserVerification userVerification = userVerificationRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User is not already registered"));

        if (userVerification.isVerified()) return true; // already verified

        if (userVerification.getVerificationToken() == null) return false;
        if (!userVerification.getVerificationToken().equals(code)) return false;
        LocalDateTime expiry = userVerification.getVerificationTokenExpiry();
        if (expiry == null || expiry.isBefore(LocalDateTime.now())) return false;

        // Code matches and is not expired - mark as verified
        userVerification.setVerified(true);
        userVerification.setVerificationToken(null);
        userVerification.setVerificationTokenExpiry(null);
        userVerificationRepository.save(userVerification);

        securityLogger.logRegistrationSuccess(email);
        return true;
    }

    @Override
    @Transactional
    public void markAsVerified(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user.not_found"));

        UserVerification userVerification = userVerificationRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User is not already registered"));

        userVerification.setVerified(true);
        userVerification.setVerificationToken(null);
        userVerification.setVerificationTokenExpiry(null);

        userVerificationRepository.save(userVerification);

        securityLogger.logRegistrationSuccess(email);
    }
}
