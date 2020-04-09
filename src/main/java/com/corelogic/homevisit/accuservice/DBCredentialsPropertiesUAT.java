package com.corelogic.homevisit.accuservice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Data
@ConfigurationProperties(prefix = "vcap.services.dbcreds-uat.credentials")
@Configuration
@Profile({"uat"})
public class DBCredentialsPropertiesUAT implements DatabaseCredentials {
    private String url;
    private String username;
    private String password;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
