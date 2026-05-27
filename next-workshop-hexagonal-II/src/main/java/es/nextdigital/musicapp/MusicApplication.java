package es.nextdigital.musicapp;

import es.nextdigital.musicapp.application.port.out.EmailNotificationPort;
import es.nextdigital.musicapp.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }

    @Bean
    public CommandLineRunner testMyCode(EmailNotificationPort emailNotificationPort) {
        return args -> {
            System.out.println("\n--- INICIANDO PRUEBA ---\n");

            User testUser = new User("1234567890D", "Fulanito", "fulanito@gmail.com");

            System.out.println("Hola, " +  testUser.getName() + "!");

            testUser.addFavoriteSong("1234");
            testUser.addFavoriteSong("2345");
            testUser.addFavoriteSong("3456");
            testUser.addFavoriteSong("4567");
            testUser.addFavoriteSong("5678");

            System.out.println("\nAñadiendo canciones favoritas...");

            if (testUser.getFavoriteSongIds().size() >= 5) {
                emailNotificationPort.sendFavouriteLimitReachedEmail(testUser);
            }

            System.out.println("\n--- FIN DE LA PRUEBA ---");
        };
    }
}
