package es.nextdigital.musicapp.application;

import es.nextdigital.musicapp.application.port.out.UserRepository;
import es.nextdigital.musicapp.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RegisterUserService {

    private final UserRepository userRepository;

    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {
        String id = UUID.randomUUID().toString();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        user.setId(id);
        userRepository.save(user);
        return id;
    }
}
