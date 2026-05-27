package es.nextdigital.musicapp.infrastructure.adapter.out;

import es.nextdigital.musicapp.application.port.out.EmailNotificationPort;
import es.nextdigital.musicapp.domain.User;
import org.springframework.stereotype.Component;

@Component
public class EmailNotification implements EmailNotificationPort {

    @Override
    public void sendFavouriteLimitReachedEmail(User user) {
        System.out.println("\n[SIMULACIÓN] Enviando email a " + user.getName() + ".....");
        System.out.println("---> ¡Has alcanzado el límite de canciones favoritas!");
    }
}
