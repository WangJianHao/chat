package com.sen.chat.chatserver.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 23:28
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String accessKey;

    private String secretKey;

    private String url;

    private String bucketName;

    private String tempBucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .region("cn-north-1")
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getUrl() {
        return url;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getTempBucketName() {
        return tempBucketName;
    }
}
