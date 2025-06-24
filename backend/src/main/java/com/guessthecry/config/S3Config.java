package com.guessthecry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Config {

    // Injecteer de waarden direct.
    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.cries.bucket}")
    private String criesBucket;

    @Value("${s3.sprites.bucket}")
    private String spritesBucket;

    public String getEndpoint() {
        return endpoint;
    }

    public String getCriesBucket() {
        return criesBucket;
    }

    public String getSpritesBucket() {
        return spritesBucket;
    }
}