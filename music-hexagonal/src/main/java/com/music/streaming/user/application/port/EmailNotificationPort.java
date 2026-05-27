package com.music.streaming.user.application.port;

import com.music.streaming.user.domain.User;

public interface EmailNotificationPort {
    void sendFavouriteLimitReachedEmail(User user);
}
