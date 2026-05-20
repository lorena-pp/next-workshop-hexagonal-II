package es.nextdigital.musicapp.application.port.out;

import es.nextdigital.musicapp.domain.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(String id);
}
