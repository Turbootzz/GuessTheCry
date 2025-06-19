package com.guessthecry.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(RolesAllowedDynamicFeature.class);
        packages("com.guessthecry.webservices", "com.guessthecry.security");
    }
}