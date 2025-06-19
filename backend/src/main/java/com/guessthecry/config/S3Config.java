package com.guessthecry.config;

import io.github.cdimascio.dotenv.Dotenv;

public class S3Config {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String endpoint = dotenv.get("MINIO_ENDPOINT");
    private static final String bucket = dotenv.get("MINIO_BUCKET");
    private static final String accessKey = dotenv.get("MINIO_ACCESS_KEY");
    private static final String secretKey = dotenv.get("MINIO_SECRET_KEY");

    public static void envCheck() {
        if (getEndpoint() == null) {
            throw new IllegalStateException("Missing MINIO_ENDPOINT in .env");
        }
        if (getBucket() == null) {
            throw new IllegalStateException("Missing MINIO_BUCKET in .env");
        }
        if (getAccessKey() == null) {
            throw new IllegalStateException("Missing MINIO_ACCESS_KEY in .env");
        }
        if (getSecretKey() == null) {
            throw new IllegalStateException("Missing MINIO_SECRET_KEY in .env");
        }
    }

    public static String getEndpoint() {
        return endpoint;
    }

    public static String getBucket() {
        return bucket;
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }
}
