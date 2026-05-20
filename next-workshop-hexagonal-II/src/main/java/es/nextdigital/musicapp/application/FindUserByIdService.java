package es.nextdigital.musicapp.application;

import es.nextdigital.musicapp.application.port.out.UserRepository;
import es.nextdigital.musicapp.domain.User;
import org.springframework.stereotype.Service;

@Service
public class FindUserByIdService {
    private final UserRepository userRepository;

    public FindUserByIdService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow();
    }
}
