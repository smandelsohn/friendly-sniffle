package com.corelogic.homevisit.accuservice.component;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Simple security token holder for AccuConnect services.
 */
public class SecurityTokenHolder {

    private String token;
    private final String tokenType = "bearer";
    private int tokenExpired = 0;
    private LocalDateTime tokenIssuedAt = LocalDateTime.now();

    public boolean isExpired() {
        LocalDateTime nowLdt = LocalDateTime.now();
        LocalDateTime expLdt = tokenIssuedAt.plus(tokenExpired, ChronoUnit.SECONDS);
        if (nowLdt.isAfter(expLdt) || nowLdt.isEqual(expLdt)) {
            token = null;
            return true;
        }
        return false;
    }

    public void updateToken(String access_token, int expires_in) {
        this.tokenIssuedAt = LocalDateTime.now();
        this.token = access_token;
        this.tokenExpired = expires_in;
    }


    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return "SecurityTokenHolder{" +
                " expired = " + isExpired() +
                ", TTL (sec) = " + tokenExpired +
                ", tokenIssuedAt=" + tokenIssuedAt +
                ", token=" + StringUtils.truncate(token, 32) +
                '}';
    }
}
