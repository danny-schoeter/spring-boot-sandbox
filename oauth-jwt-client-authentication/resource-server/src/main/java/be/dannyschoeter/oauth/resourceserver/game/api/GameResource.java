package be.dannyschoeter.oauth.resourceserver.game.api;


import be.dannyschoeter.oauth.resourceserver.game.domain.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/game", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameResource {

    private static final Logger LOG = LoggerFactory.getLogger(GameResource.class);

    @GetMapping(path = "/{id}")
    public Game getGame(@PathVariable("id") UUID id,
                        @RequestHeader("x-correlation-id") String correlationId,
                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        LOG.info("-------------------------------------------------");
        LOG.info("Game requested");
        LOG.info("id: {}", id);
        LOG.info("x-correlation-id: {}", correlationId);
        LOG.info("Authorization: {}", authorization);
        LOG.info("-------------------------------------------------");
        return new Game(id, "The Legend of Zelda: Tears of the Kingdom");
    }
}
