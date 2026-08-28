package com.Marci.FinVault.API.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class S3StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3StorageService(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public UploadedFile uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );
            String path = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            return new UploadedFile(path, bucketName, fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file before sending it to S3", e);
        } catch (SdkException e) {
            throw new IllegalStateException(
                    "AWS S3 credentials or bucket configuration are invalid. Set AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY, or configure an IAM role.",
                    e
            );
        }
    }

    public String createDownloadUrl(String bucket, String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();
        return s3Presigner.presignGetObject(presignRequest).url().toExternalForm();
    }

    public ResponseBytes<GetObjectResponse> downloadFile(String bucket, String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();
        return s3Client.getObjectAsBytes(getObjectRequest);
    }

    public record UploadedFile(String path, String bucket, String objectKey) {

    }

}
