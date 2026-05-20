package es.nextdigital.musicapp.infrastructure.adapter.out;

import es.nextdigital.musicapp.application.port.out.UserRepository;
import es.nextdigital.musicapp.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<String, User> users;

    public UserInMemoryRepository() {
        this.users = new HashMap<>();
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }
}
