package be.dannyschoeter.oauth.client.resource;

import be.dannyschoeter.oauth.client.security.JWKResolver;
import be.dannyschoeter.oauth.client.security.OauthClientCredentialsManager;
import be.dannyschoeter.oauth.client.security.RSAKeyReader;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.NimbusJwtClientAuthenticationParametersConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

public class GameClientConfiguration {

    public static final String CLIENT_REGISTRATION_ID = "game";

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public GameClientConfiguration(OAuth2AuthorizedClientService oAuth2AuthorizedClientService, ClientRegistrationRepository clientRegistrationRepository) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public RequestInterceptor requestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(CLIENT_REGISTRATION_ID);
        OauthClientCredentialsManager clientCredentialsFeignManager = new OauthClientCredentialsManager(authorizedClientManager, clientRegistration);
        return requestTemplate -> requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + clientCredentialsFeignManager.getAccessToken());
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(DefaultClientCredentialsTokenResponseClient responseClient) {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials(clientCredentials -> clientCredentials.accessTokenResponseClient(responseClient))
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//        authorizedClientManager.setAuthorizationFailureHandler();
//        authorizedClientManager.setAuthorizationSuccessHandler();
        return authorizedClientManager;
    }

    @Bean
    DefaultClientCredentialsTokenResponseClient authorizationCodeTokenResponseClient(OAuth2ClientCredentialsGrantRequestEntityConverter requestEntityConverter) {
        DefaultClientCredentialsTokenResponseClient tokenResponseClient = new DefaultClientCredentialsTokenResponseClient();
        tokenResponseClient.setRequestEntityConverter(requestEntityConverter);
        return tokenResponseClient;
    }

    @Bean
    OAuth2ClientCredentialsGrantRequestEntityConverter requestEntityConverter(
            @Value("${jwt.privatekey.location}") String privateKeyLocation,
            @Value("${jwt.publickey.location}") String publicKeyLocation,
            ResourceLoader resourceLoader) {
        OAuth2ClientCredentialsGrantRequestEntityConverter requestEntityConverter = new OAuth2ClientCredentialsGrantRequestEntityConverter();
        JWKResolver jwkResolver = new JWKResolver(new RSAKeyReader(privateKeyLocation, publicKeyLocation, resourceLoader));
        NimbusJwtClientAuthenticationParametersConverter<AbstractOAuth2AuthorizationGrantRequest> converter = new NimbusJwtClientAuthenticationParametersConverter<>(jwkResolver);
        converter.setJwtClientAssertionCustomizer(context -> context.getClaims().claim("custom-claim", "123-test"));
        requestEntityConverter.addParametersConverter((NimbusJwtClientAuthenticationParametersConverter) converter);
        return requestEntityConverter;
    }
}
