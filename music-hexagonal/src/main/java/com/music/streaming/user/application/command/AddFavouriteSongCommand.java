package com.music.streaming.user.application.command;

import com.music.streaming.user.application.port.EmailNotificationPort;
import com.music.streaming.user.application.port.UserRepositoryPort;
import com.music.streaming.user.domain.User;
import com.music.streaming.user.domain.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AddFavouriteSongCommand {
    private final UserRepositoryPort userRepositoryPort;
    private final EmailNotificationPort emailNotificationPort;
    private static final int FAVOURITE_LIMIT = 5;

    public AddFavouriteSongCommand(UserRepositoryPort userRepositoryPort, EmailNotificationPort emailNotificationPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.emailNotificationPort = emailNotificationPort;
    }

    public void handle(String userId, String songId) throws UserNotFoundException {
        User user = userRepositoryPort.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        user.addFavouriteSong(songId);

        userRepositoryPort.updateUser(user);

        if (user.getFavouriteSongIds().size() >= FAVOURITE_LIMIT) {
            emailNotificationPort.sendFavouriteLimitReachedEmail(user);
        }
    }
}
