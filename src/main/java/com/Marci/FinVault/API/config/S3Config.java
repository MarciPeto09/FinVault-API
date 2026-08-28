package com.Marci.FinVault.API.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.access-key-id:}")
    private String accessKeyId;

    @Value("${aws.secret-access-key:}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        Region r = Region.of(region);

        if (!StringUtils.hasText(accessKeyId)) {
            accessKeyId = resolveFromEnvironmentOrSystem("AWS_ACCESS_KEY_ID", "aws.access-key-id");
        }
        if (!StringUtils.hasText(secretAccessKey)) {
            secretAccessKey = resolveFromEnvironmentOrSystem("AWS_SECRET_ACCESS_KEY", "aws.secret-access-key");
        }

        String sessionToken = resolveFromEnvironmentOrSystem("AWS_SESSION_TOKEN", "aws.session-token");

        var builder = S3Client.builder().region(r);

        if (StringUtils.hasText(accessKeyId) && StringUtils.hasText(secretAccessKey)) {
            if (StringUtils.hasText(sessionToken)) {
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken))
                );
                logger.info("Using AWS session credentials for S3 client");
            } else {
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey))
                );
                logger.info("Using AWS static credentials for S3 client");
            }
        } else {
            builder.credentialsProvider(DefaultCredentialsProvider.create());
            logger.warn("No AWS credentials configured. S3 bucket validation will be skipped until an actual S3 operation is used.");
        }

        S3Client s3 = builder.build();

        if (!StringUtils.hasText(accessKeyId) || !StringUtils.hasText(secretAccessKey)) {
            return s3;
        }

        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            logger.info("S3 bucket '{}' exists", bucketName);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                logger.info("S3 bucket '{}' not found; creating...", bucketName);
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .createBucketConfiguration(CreateBucketConfiguration.builder()
                                .locationConstraint(r.id())
                                .build())
                        .build();
                s3.createBucket(createBucketRequest);
                logger.info("S3 bucket '{}' created", bucketName);
            } else {
                logger.warn("Error checking S3 bucket existence: {}", e.awsErrorDetails().errorMessage());
            }
        } catch (Exception e) {
            logger.warn("Unexpected error while ensuring S3 bucket: {}", e.getMessage());
        }

        return s3;
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    private String resolveFromEnvironmentOrSystem(String envName, String systemPropertyName) {
        String value = System.getenv(envName);
        if (!StringUtils.hasText(value)) {
            value = System.getProperty(envName);
        }
        if (!StringUtils.hasText(value)) {
            value = System.getProperty(systemPropertyName);
        }
        return value;
    }

}
