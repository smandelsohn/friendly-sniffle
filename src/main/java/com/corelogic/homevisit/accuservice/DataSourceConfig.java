package com.corelogic.homevisit.accuservice;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("cloud")
public class DataSourceConfig {

    private final DatabaseCredentials dbCredentialsProperties;

    public DataSourceConfig(DatabaseCredentials dbCredentialsProperties) {
        this.dbCredentialsProperties = dbCredentialsProperties;
    }

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .url(dbCredentialsProperties.getUrl())
                .username(dbCredentialsProperties.getUsername())
                .password(dbCredentialsProperties.getPassword())
                .build();
    }
}