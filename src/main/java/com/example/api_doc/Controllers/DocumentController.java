package com.example.api_doc.Controllers;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.DocumentNotAddedException;
import com.example.api_doc.Exceptions.DocumentNotDeletedException;
import com.example.api_doc.Exceptions.DocumentUpdateFailException;
import com.example.api_doc.Requests.DocumentRequest;
import com.example.api_doc.Services.IDocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Date;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private IDocumentService iDocumentService;


    @PostMapping
    public Optional<Document> addDocument(@RequestBody DocumentRequest documentRequest) throws DocumentNotAddedException, NoSuchAlgorithmException, IOException {

        Optional<Document> document = iDocumentService.addDocument(documentRequest);
        return document ;
    }

    @GetMapping
    public List<Document> getAllDocument() {
        return iDocumentService.getAllDocument();
    }

    @GetMapping("/{documentID}")
    public  Optional<Document> getDocumentById(@PathVariable Integer documentID) {
        return iDocumentService.getDocumentById(documentID);
    }

    @GetMapping("/hashed/{hashedDocument}")
    public  Optional<Document> getDocumentByHashedDocument(@PathVariable String hashedDocument) {
        return iDocumentService.getDocumentByHashedDocument(hashedDocument);
    }

    @PutMapping
    public Optional<Document> updateDocument(@RequestBody DocumentRequest document) throws DocumentUpdateFailException, NoSuchAlgorithmException, JsonProcessingException {
        return iDocumentService.updateDocument(document);
    }

    @DeleteMapping("/{documentID}")
    public Integer deleteDocument(@PathVariable Integer documentID) throws DocumentNotDeletedException {
        return iDocumentService.deleteDocument(documentID);
    }
    @GetMapping("/byNom")
    public List<Document> getDocumentByNom(@RequestParam String nom) {
        return iDocumentService.getDocumentByNom(nom);
    }

    @GetMapping("/byDateCreation")
    public List<Document> getDocumentByDateCreation(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        return iDocumentService.getDocumentByDateCreation(date);
    }

    @GetMapping("/byType")
    public List<Document> getDocumentByType(@RequestParam String type) {
        return iDocumentService.getDocumentByType(type);
    }


}
