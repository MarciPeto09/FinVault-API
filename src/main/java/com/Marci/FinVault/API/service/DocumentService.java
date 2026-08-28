package com.Marci.FinVault.API.service;


import com.Marci.FinVault.API.entity.Document;
import com.Marci.FinVault.API.enums.DocumentStatus;
import com.Marci.FinVault.API.enums.DocumentType;
import com.Marci.FinVault.API.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.security.core.Authentication;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final S3StorageService s3StorageService;

    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    public DocumentService(DocumentRepository documentRepository, S3StorageService s3StorageService) {
        this.documentRepository = documentRepository;
        this.s3StorageService = s3StorageService;
    }

    public void saveFileMetadata(MultipartFile file, String path, String bucket, String objectKey,
                                 Authentication authentication, String type) {
        Document document = new Document();
        document.setDocumentName(file.getOriginalFilename());
        document.setDocumentSize(String.valueOf(file.getSize()));
        document.setDocumentPath(path);
        document.setS3Bucket(bucket);
        document.setS3ObjectKey(objectKey);
        document.setOwnerSubject(authentication == null ? null : authentication.getName());
        document.setDocumentStatus(DocumentStatus.AVAILABLE);
        document.setDocumentType(DocumentType.valueOf(type.toUpperCase()));

        try {
            documentRepository.save(document);
        } catch (Exception e) {
            logger.error("Error saving document metadata: {}", e.getMessage());
            throw new RuntimeException("Error saving document metadata", e);
        }
    }

    public String uploadFile(MultipartFile file, String type, Authentication authentication) {
        S3StorageService.UploadedFile uploadedFile = s3StorageService.uploadFile(file);
        saveFileMetadata(file, uploadedFile.path(), uploadedFile.bucket(),
                uploadedFile.objectKey(), authentication, type);
        return uploadedFile.path();
    }

    public List<Document> getAllDocuments(String ownerSubject) {
        try {
            return documentRepository.findAllByOwnerSubject(ownerSubject);
        } catch (Exception e) {
            logger.error("Error retrieving documents: {}", e.getMessage());
            throw new RuntimeException("Error retrieving documents", e);
        }
    }

    public ResponseEntity<?> getFile(Long id, Authentication authentication, boolean download) {
        try {
            Document document = getOwnedDocument(id, authentication.getName());
            ResponseBytes<GetObjectResponse> file = s3StorageService.downloadFile(
                    document.getS3Bucket(), document.getS3ObjectKey());
            String contentType = file.response().contentType() == null
                    ? MediaType.APPLICATION_OCTET_STREAM_VALUE
                    : file.response().contentType();
            ContentDisposition disposition = download
                    ? ContentDisposition.attachment().filename(document.getDocumentName()).build()
                    : ContentDisposition.inline().filename(document.getDocumentName()).build();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(file.response().contentLength())
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                    .body(new ByteArrayResource(file.asByteArray()));
        } catch (DocumentService.DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", "Unable to retrieve document"));
        }
    }

    public void deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Document with ID " + id + " not found.");
        }
    }

    public Document getOwnedDocument(Long id, String ownerSubject) {
        return documentRepository.findByIdAndOwnerSubject(id, ownerSubject)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    public boolean isOwnedBy(Document document, String ownerSubject) {
        return document.getOwnerSubject() != null && document.getOwnerSubject().equals(ownerSubject);
    }

    public static class DocumentNotFoundException extends RuntimeException {
        public DocumentNotFoundException(Long id) {
            super("Document with ID " + id + " not found.");
        }
    }
}
