package com.music.streaming.user.infrastructure.notification;

import com.music.streaming.user.application.port.EmailNotificationPort;
import com.music.streaming.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationAdapter implements EmailNotificationPort {
    @Override
    public void sendFavouriteLimitReachedEmail(User user) {
        System.out.println("\n[SIMULACIÓN] Enviando email a " + user.getUsername() + ".....");
        System.out.println("---> ¡Has alcanzado el límite de canciones favoritas!");
    }
}
