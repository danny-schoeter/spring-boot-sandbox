package be.dannyschoeter.oauth.client.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Objects;

public class OauthClientCredentialsManager {

    private static final Logger LOG = LoggerFactory.getLogger(OauthClientCredentialsManager.class);


    private final OAuth2AuthorizedClientManager manager;
    private final ClientRegistration clientRegistration;

    public OauthClientCredentialsManager(OAuth2AuthorizedClientManager manager, ClientRegistration clientRegistration) {
        this.manager = manager;
        this.clientRegistration = clientRegistration;
    }

    public String getAccessToken() {
        try {
            OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientRegistration.getRegistrationId())
                    .principal(createPrincipal())
                    .build();
            OAuth2AuthorizedClient client = manager.authorize(oAuth2AuthorizeRequest);
            if (Objects.isNull(client)) {
                throw new IllegalStateException("client credentials flow on " + clientRegistration.getRegistrationId() + " failed, client is null");
            }
            return client.getAccessToken().getTokenValue();
        } catch (Exception exp) {
            LOG.error("client credentials error " + exp.getMessage());
        }
        return null;
    }

    private Authentication createPrincipal() {
        return new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    }
}