package com.gomz.festivallineuptracker.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI festivalLineupTrackerOpenAPI() {

        return new OpenAPI().info(new Info().title("Festival Lineup Tracker API").version("1.0")
                        .description("REST API for managing music festivals, artists and festival lineups."));
    }

}
