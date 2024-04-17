package com.example.api_doc.Repository;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.DocumentNotAddedException;
import com.example.api_doc.Exceptions.DocumentNotDeletedException;
import com.example.api_doc.Exceptions.DocumentUpdateFailException;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IDocumentDAO {
    Optional<Document> addDocument(Document document, MultipartFile file) throws DocumentNotAddedException;

    List<Document> getAllDocument();

    Optional<Document> getDocumentById(Integer DocumentID);

    Optional<Document> getDocumentByHashedDocument(String hashedDocument);

    Integer deleteDocument(Integer DocumentID) throws DocumentNotDeletedException;

    Optional<Document> updateDocument(Document document) throws DocumentUpdateFailException;

    List<Document> getDocumentByNom(String nom);



}
