package be.dannyschoeter.oauth.client.game.domain;

import java.util.UUID;

public class Game {

    private final UUID id;
    private final String title;

    public Game(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
