package be.dannyschoeter.oauth.authserver.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/authorization/oauth/v2", produces = MediaType.APPLICATION_JSON_VALUE)
public class TokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TokenProvider.class);

    @PostMapping("/token")
    public TokenResponse requestToken(@RequestParam Map<String, String> requestParams) {
        LOG.info("-------------------------------------------");
        LOG.info("Token requested.");
        LOG.info("Request parameters: \n{}", requestParams);
        LOG.info("-------------------------------------------");
        /**
         *         Expected parameters:
         *         grant_type
         *         scope
         *         client_assertion_type
         *         client_assertion
         */
        return new TokenResponse(
                "POtyWHuQd94pk6AtHbew3B==",
                requestParams.get("scope"),
                300, // 5 minutes
                "Bearer"
        );
    }

}
