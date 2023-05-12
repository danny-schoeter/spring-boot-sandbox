package be.dannyschoeter.oauth.client.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.util.function.Function;

public class JWKResolver implements Function<ClientRegistration, JWK> {

    private static final Logger LOG = LoggerFactory.getLogger(JWKResolver.class);
    private final RSAKeyReader rsaKeyReader;

    public JWKResolver(RSAKeyReader rsaKeyReader) {
        this.rsaKeyReader = rsaKeyReader;
    }

    @Override
    public JWK apply(ClientRegistration clientRegistration) {
        try {
            if (clientRegistration.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.PRIVATE_KEY_JWT)) {
                RSAPublicKey publicKey = rsaKeyReader.getPublicKey();
                RSAPrivateKey privateKey = rsaKeyReader.getPrivateKey();
                return new RSAKey.Builder(publicKey)
                        .privateKey(privateKey)
                        .keyID(UUID.randomUUID().toString())
                        .build();
            }
        } catch (IOException e) {
            LOG.error("Could not resolve JWK", e);
        }
        return null;
    }
}
