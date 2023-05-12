package be.dannyschoeter.oauth.client.game.api;


import be.dannyschoeter.oauth.client.game.domain.Game;
import be.dannyschoeter.oauth.client.resource.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/game", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameResource {

    private static final Logger LOG = LoggerFactory.getLogger(GameResource.class);

    private final GameClient gameClient;

    public GameResource(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @GetMapping(path = "/{id}")
    public Game getGame(@PathVariable("id") UUID id) {
        LOG.info("-------------------------------------------------");
        LOG.info("Game requested, id: {}", id);
        LOG.info("-------------------------------------------------");
        return gameClient.getGame(id, "game-request-" + UUID.randomUUID());
    }
}
