package es.nextdigital.musicapp.application.port.out;

import es.nextdigital.musicapp.domain.User;

public interface EmailNotificationPort {
    void sendFavoriteLimitReachedEmail(User user);
}
