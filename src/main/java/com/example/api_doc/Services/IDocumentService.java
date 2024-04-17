package com.example.api_doc.Services;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.DocumentNotAddedException;
import com.example.api_doc.Exceptions.DocumentNotDeletedException;
import com.example.api_doc.Exceptions.DocumentUpdateFailException;
import com.example.api_doc.Requests.DocumentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IDocumentService {

    Optional<Document> addDocument(MultipartFile  file) throws DocumentNotAddedException, IOException, NoSuchAlgorithmException;

    List<Document> getAllDocument();

    Optional<Document> getDocumentById(Integer DocumentID);
    Optional<Document> getDocumentByHashedDocument(String hashedDocument);

    Integer deleteDocument(Integer DocumentID) throws DocumentNotDeletedException;

    Optional<Document> updateDocument(DocumentRequest document) throws DocumentUpdateFailException, NoSuchAlgorithmException, JsonProcessingException;

    List<Document> getDocumentByNom(String nom);
     List<Document> searchDocuments(String nom, String type, Date date_de_creation) ;



    }
