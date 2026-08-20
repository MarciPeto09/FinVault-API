package com.Marci.FinVault.API.entity;

import com.Marci.FinVault.API.enums.DocumentStatus;
import com.Marci.FinVault.API.enums.DocumentType;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import javax.management.relation.Role;

@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(nullable = false)
    private String documentName;

    @Column(nullable = false)
    private String documentPath;

    @Column(nullable = false)
    private String documentSize;

    @Column(nullable = false)
    private String uploadedBy;

    @Column(nullable = false)
    private String uploadedAt;

    @Column(nullable = false)
    private String updatedAt;

    @Column(nullable = false)
    private String updatedBy;

    @Column(nullable = false)
    private String documentDescription;

    @Column(nullable = false)
    private String documentTags;

    @Column(nullable = false)
    private String documentVersion;

    @Column(nullable = false)
    private String documentChecksum;

    @Column(nullable = false)
    private String documentDestination;

    @Column(nullable = false)
    private String documentRetentionPolicy;

    @Column(nullable = false)
    private String documentAccessControl;

    @Column(nullable = false)
    private String documentAuditTrail;

    @Column(nullable = false)
    private String documentCompliance;

    @Column(nullable = false)
    private String documentEncryption;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(String documentSize) {
        this.documentSize = documentSize;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentTags() {
        return documentTags;
    }

    public void setDocumentTags(String documentTags) {
        this.documentTags = documentTags;
    }

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {
        this.documentVersion = documentVersion;
    }

    public String getDocumentChecksum() {
        return documentChecksum;
    }

    public void setDocumentChecksum(String documentChecksum) {
        this.documentChecksum = documentChecksum;
    }

    public String getDocumentDestination() {
        return documentDestination;
    }

    public void setDocumentDestination(String documentDestination) {
        this.documentDestination = documentDestination;
    }

    public String getDocumentRetentionPolicy() {
        return documentRetentionPolicy;
    }

    public void setDocumentRetentionPolicy(String documentRetentionPolicy) {
        this.documentRetentionPolicy = documentRetentionPolicy;
    }

    public String getDocumentAccessControl() {
        return documentAccessControl;
    }

    public void setDocumentAccessControl(String documentAccessControl) {
        this.documentAccessControl = documentAccessControl;
    }

    public String getDocumentAuditTrail() {
        return documentAuditTrail;
    }

    public void setDocumentAuditTrail(String documentAuditTrail) {
        this.documentAuditTrail = documentAuditTrail;
    }

    public String getDocumentCompliance() {
        return documentCompliance;
    }

    public void setDocumentCompliance(String documentCompliance) {
        this.documentCompliance = documentCompliance;
    }

    public String getDocumentEncryption() {
        return documentEncryption;
    }

    public void setDocumentEncryption(String documentEncryption) {
        this.documentEncryption = documentEncryption;
    }
}
