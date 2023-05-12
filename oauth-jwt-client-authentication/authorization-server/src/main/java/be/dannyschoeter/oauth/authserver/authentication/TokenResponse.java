package be.dannyschoeter.oauth.authserver.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    @JsonProperty("access_token")
    private final String token;
    @JsonProperty("scope")
    private final String scope;
    @JsonProperty("expires_in")
    private final long expirationTime;
    @JsonProperty("token_type")
    private final String tokenType;

    public TokenResponse(String token, String scope, long expirationTime, String tokenType) {
        this.token = token;
        this.scope = scope;
        this.expirationTime = expirationTime;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }


    public String getScope() {
        return scope;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public String getTokenType() {
        return tokenType;
    }
}
