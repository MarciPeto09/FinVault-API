package com.Marci.FinVault.API.entity;

import com.Marci.FinVault.API.enums.DocumentStatus;
import com.Marci.FinVault.API.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentName;

    private String documentSize;

    @JsonIgnore
    private String documentPath;

    private String s3Bucket;

    private String s3ObjectKey;

    @JsonIgnore
    private String ownerSubject;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
}