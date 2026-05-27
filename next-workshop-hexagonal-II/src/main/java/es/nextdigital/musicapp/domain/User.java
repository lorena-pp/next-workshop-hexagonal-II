package es.nextdigital.musicapp.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private List<String> favoriteSongIds = new ArrayList<>();

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favoriteSongIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setFavoriteSongIds(List<String> favoriteSongIds) {
        this.favoriteSongIds = favoriteSongIds;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addFavoriteSong(String songId) {
        if (!this.favoriteSongIds.contains(songId)) {
            this.favoriteSongIds.add(songId);
        }
    }

    public List<String> getFavoriteSongIds() {
        return favoriteSongIds;
    }
}
