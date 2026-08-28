package com.Marci.FinVault.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Marci.FinVault.API.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByIdAndOwnerSubject(Long id, String ownerSubject);

    List<Document> findAllByOwnerSubject(String ownerSubject);
}
