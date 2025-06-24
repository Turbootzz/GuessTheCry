package com.guessthecry.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

public class S3Config {
    @Value("${s3.endpoint}")
    private String endpointValue;
    @Value("${s3.cries.bucket}")
    private String criesBucketValue;
    @Value("${s3.sprites.bucket}")
    private String spritesBucketValue;

    private static String endpoint;
    private static String criesBucket;
    private static String spritesBucket;

    @PostConstruct
    public void init() {
        endpoint = this.endpointValue;
        criesBucket = this.criesBucketValue;
        spritesBucket = this.spritesBucketValue;
    }

    public static String getEndpoint() { return endpoint; }
    public static String getCriesBucket() { return criesBucket; }
    public static String getSpritesBucket() { return spritesBucket; }
}
