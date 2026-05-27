package es.nextdigital.musicapp.application;

import es.nextdigital.musicapp.application.port.out.EmailNotificationPort;
import es.nextdigital.musicapp.domain.User;
import es.nextdigital.musicapp.domain.UserNotFoundException;
import es.nextdigital.musicapp.infrastructure.adapter.out.UserInMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class AddFavouriteSongService {
    private final UserInMemoryRepository userRepositoryPort;
    private final EmailNotificationPort emailNotificationPort;
    private static final int FAVOURITE_LIMIT = 5;

    public AddFavouriteSongService(UserInMemoryRepository userRepositoryPort, EmailNotificationPort emailNotificationPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.emailNotificationPort = emailNotificationPort;
    }

    public void addFavouriteSong(String userId, String songId) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.addFavoriteSong(songId);

        userRepositoryPort.save(user);

        if (user.getFavoriteSongIds().size() >= FAVOURITE_LIMIT) {
            emailNotificationPort.SendFavouriteLimitReachedEmail(user);
        }
    }
}
