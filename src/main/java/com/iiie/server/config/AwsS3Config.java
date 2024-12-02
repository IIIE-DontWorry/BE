package com.iiie.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    private final AwsCredentials awsCredentials;

    public AwsS3Config(AwsCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(
                                awsCredentials.getAccessKey(),
                                awsCredentials.getSecretKey()
                            )
                        )
                )
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .build();
    }
}
