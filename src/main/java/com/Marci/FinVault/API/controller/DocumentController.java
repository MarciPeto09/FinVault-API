package com.Marci.FinVault.API.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Marci.FinVault.API.entity.Document;
import com.Marci.FinVault.API.service.DocumentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/getAll")
    public List<Document> getAllDocuments(Authentication authentication) {
        return documentService.getAllDocuments(authentication.getName());
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("type") String type,
            Authentication authentication) {
        return documentService.uploadFile(file, type, authentication);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadDocument(@PathVariable Long id, Authentication authentication) {
        return documentService.getFile(id, authentication, true);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id, Authentication authentication) {
        try {
            Document document = documentService.getOwnedDocument(id, authentication.getName());
            documentService.deleteDocument(id);
            return ResponseEntity.ok(java.util.Map.of("message", "Document with ID " + id + " deleted successfully."));
        } catch (DocumentService.DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
