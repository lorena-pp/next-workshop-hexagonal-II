package com.music.streaming;

import com.music.streaming.catalog.application.port.SongRepository;
import com.music.streaming.catalog.domain.Song;
import com.music.streaming.user.application.port.EmailNotificationPort;
import com.music.streaming.user.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }

    @Bean
    public CommandLineRunner testMyCode(
            EmailNotificationPort emailNotificationPort,
            SongRepository songRepository
    ) {
        return args -> {
            System.out.println("\n--- INICIANDO PRUEBA REAL ---\n");

            songRepository.createSong(Song.builder().title("Bohemian Rhapsody").durationSeconds(354).artistId("ART-1").albumId("ALB-1").genreId("GEN-1").build());
            songRepository.createSong(Song.builder().title("Hotel California").durationSeconds(390).artistId("ART-2").albumId("ALB-2").genreId("GEN-2").build());
            songRepository.createSong(Song.builder().title("Stairway to Heaven").durationSeconds(482).artistId("ART-3").albumId("ALB-3").genreId("GEN-3").build());
            songRepository.createSong(Song.builder().title("Imagine").durationSeconds(183).artistId("ART-4").albumId("ALB-4").genreId("GEN-4").build());
            songRepository.createSong(Song.builder().title("Smells Like Teen Spirit").durationSeconds(301).artistId("ART-5").albumId("ALB-5").genreId("GEN-5").build());

            User testUser = User.builder()
                    .username("Fulanito")
                    .email("fulanito@upm.es")
                    .favouriteSongIds(new ArrayList<>())
                    .build();

            System.out.println("Hola, " +  testUser.getUsername() + "!");

            List<Song> catalogo = songRepository.getSongs();

            testUser.addFavouriteSong(catalogo.get(0).getId());
            testUser.addFavouriteSong(catalogo.get(1).getId());
            testUser.addFavouriteSong(catalogo.get(2).getId());
            testUser.addFavouriteSong(catalogo.get(3).getId());
            testUser.addFavouriteSong(catalogo.get(4).getId());

            System.out.println("\nAñadiendo canciones favoritas...");

            if (testUser.getFavouriteSongIds().size() >= 5) {
                emailNotificationPort.sendFavouriteLimitReachedEmail(testUser);
            }

            System.out.println("\n--- FIN DE LA PRUEBA ---");
        };
    }
}
