package be.dannyschoeter.oauth.client.resource;

import be.dannyschoeter.oauth.client.game.domain.Game;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;


@FeignClient(name = "game-client-v1", url = "http://localhost:8082", path = "/api/v1/game", configuration = GameClientConfiguration.class)
public interface GameClient {

    @GetMapping("/{id}")
    Game getGame(@PathVariable("id") UUID id, @RequestHeader("x-correlation-id") String correlationId);
}
