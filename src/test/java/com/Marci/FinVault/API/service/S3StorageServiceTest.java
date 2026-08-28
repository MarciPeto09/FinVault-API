package com.Marci.FinVault.API.service;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentCaptor;

class S3StorageServiceTest {

    @Test
    void createsUrlFromS3Presigner() throws MalformedURLException {
        S3Presigner presigner = mock(S3Presigner.class);
        PresignedGetObjectRequest presignedObject = mock(PresignedGetObjectRequest.class);
        when(presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedObject);
        when(presignedObject.url()).thenReturn(new java.net.URL("https://bucket.s3.amazonaws.com/key?signature=test"));

        S3StorageService service = new S3StorageService(mock(S3Client.class), presigner);

        assertThat(service.createDownloadUrl("bucket", "key"))
                .isEqualTo("https://bucket.s3.amazonaws.com/key?signature=test");
        ArgumentCaptor<GetObjectPresignRequest> requestCaptor = ArgumentCaptor.forClass(GetObjectPresignRequest.class);
        verify(presigner).presignGetObject(requestCaptor.capture());
        GetObjectPresignRequest request = requestCaptor.getValue();
        assertThat(request.signatureDuration().toMinutes()).isEqualTo(5);
        assertThat(request.getObjectRequest().bucket()).isEqualTo("bucket");
        assertThat(request.getObjectRequest().key()).isEqualTo("key");
    }
}