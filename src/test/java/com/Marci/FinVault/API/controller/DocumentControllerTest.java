package com.Marci.FinVault.API.controller;

import com.Marci.FinVault.API.entity.Document;
import com.Marci.FinVault.API.service.DocumentService;
import com.Marci.FinVault.API.service.S3StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentService documentService;

    @MockitoBean
    private S3StorageService s3StorageService;

    @Test
    void authenticatedOwnerReceivesPresignedUrl() throws Exception {
        Document document = document("owner-sub");
        when(documentService.getOwnedDocument(1L, "owner-sub")).thenReturn(document);
        when(documentService.isOwnedBy(document, "owner-sub")).thenReturn(true);
        when(s3StorageService.createDownloadUrl("bucket", "object-key"))
                .thenReturn("https://bucket.s3.amazonaws.com/object-key?X-Amz-Signature=test");

        mockMvc.perform(get("/api/documents/1/download-url").with(user("owner-sub")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://bucket.s3.amazonaws.com/object-key?X-Amz-Signature=test"));
    }

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        mockMvc.perform(get("/api/documents/1/download-url"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void anotherUsersDocumentIsForbidden() throws Exception {
        Document document = document("other-sub");
        when(documentService.getOwnedDocument(1L, "owner-sub")).thenReturn(document);
        when(documentService.isOwnedBy(document, "owner-sub")).thenReturn(false);

        mockMvc.perform(get("/api/documents/1/download-url").with(user("owner-sub")))
                .andExpect(status().isForbidden());
    }

    @Test
    void missingDocumentReturnsNotFound() throws Exception {
        when(documentService.getOwnedDocument(1L, "owner-sub"))
                .thenThrow(new DocumentService.DocumentNotFoundException(1L));

        mockMvc.perform(get("/api/documents/1/download-url").with(user("owner-sub")))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithoutAuthenticationIsRejected() throws Exception {
        mockMvc.perform(delete("/api/documents/delete/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void anotherUsersDocumentDeleteIsForbidden() throws Exception {
        Document document = document("other-sub");
        when(documentService.getOwnedDocument(1L, "owner-sub")).thenReturn(document);
        when(documentService.isOwnedBy(document, "owner-sub")).thenReturn(false);

        mockMvc.perform(delete("/api/documents/delete/1").with(user("owner-sub")))
                .andExpect(status().isForbidden());
    }

    private Document document(String ownerSubject) {
        Document document = new Document();
        document.setS3Bucket("bucket");
        document.setS3ObjectKey("object-key");
        document.setOwnerSubject(ownerSubject);
        return document;
    }
}