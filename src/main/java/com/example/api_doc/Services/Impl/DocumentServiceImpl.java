package com.example.api_doc.Services.Impl;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.DocumentNotAddedException;
import com.example.api_doc.Exceptions.DocumentNotDeletedException;
import com.example.api_doc.Exceptions.DocumentUpdateFailException;
import com.example.api_doc.Repository.IDocumentDAO;
import com.example.api_doc.Requests.Attribute;
import com.example.api_doc.Requests.DocumentRequest;
import com.example.api_doc.Services.IDocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements IDocumentService {



    @Value("${dossier.chemin}")
    private String dossierPath;

    @Autowired
    private IDocumentDAO iDocumentDAO;




    @Override
    public Optional<Document> addDocument(DocumentRequest document) throws DocumentNotAddedException, IOException, NoSuchAlgorithmException {
        Document documentMapped = getDocumentMapped(document);
        Optional<Document> documentByHashedDocument = getDocumentByHashedDocument(documentMapped.getHashedDocument());
        if (documentByHashedDocument.isPresent()) {
            throw new DocumentNotAddedException("Document existe deja avec ce code de hachage! ");
        }else {
            String nomFichier = documentMapped.getNomDocument()+"."+documentMapped.getTypeDocument();
            String contenuFichier = documentMapped.getMetadata().replace('{', ' ').replace('}', ' ').replace(',', '\n');

            enregistrerFichier(dossierPath, nomFichier, contenuFichier);
            return iDocumentDAO.addDocument(documentMapped);
        }

    }

    @Override
    public List<Document> getAllDocument() {
        return iDocumentDAO.getAllDocument();
    }

    @Override
    public Optional<Document> getDocumentById(Integer DocumentID) {
        return iDocumentDAO.getDocumentById(DocumentID);
    }

    @Override
    public Optional<Document> getDocumentByHashedDocument(String hashedDocument) {
        return iDocumentDAO.getDocumentByHashedDocument(hashedDocument);
    }

    @Override
    public Integer deleteDocument(Integer documentID) throws DocumentNotDeletedException {
        return iDocumentDAO.deleteDocument(documentID);
    }

    @Override
    public Optional<Document> updateDocument(DocumentRequest document) throws DocumentUpdateFailException, NoSuchAlgorithmException, JsonProcessingException {
        Document documentMapped = getDocumentMapped(document);
        return iDocumentDAO.updateDocument(documentMapped);
    }

    @Override
    public List<Document> getDocumentByNom(String nom) {
        return iDocumentDAO.getDocumentByNom(nom);
    }

    @Override
    public List<Document> getDocumentByDateCreation(Date date) {
        return iDocumentDAO.getDocumentByDateCreation(date);

    }

    @Override
    public List<Document> getDocumentByType(String type) {
        return iDocumentDAO.getDocumentByType(type);
    }

    public String getHashedDocument(Document document) throws NoSuchAlgorithmException {
        // Créez un objet MessageDigest avec l'algorithme de hachage souhaité (SHA-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Exemple de métadonnées (à remplacer par vos propres données)
        String hashedDocument = document.getNomDocument()+document.getTypeDocument()+document.getDateCreation()+document.getMetadata();
        // Convertissez les métadonnées en tableau de bytes
        byte[] metadataBytes = hashedDocument.getBytes();
        // Mettez à jour le hachage avec les métadonnées
        digest.update(metadataBytes);
        // Récupérez le hachage final sous forme de tableau de bytes
        byte[] hashedBytes = digest.digest();
        // Convertissez les bytes en une représentation hexadécimale pour l'affichage
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Document getDocumentMapped(DocumentRequest document) throws JsonProcessingException, NoSuchAlgorithmException {
        ModelMapper modelMapper = new ModelMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        for (Attribute attribute : document.getMetadataSup().getAttributes()){
            jsonObject.put(attribute.getKey(), attribute.getValue());
        }
        String jsonString = objectMapper.writeValueAsString(jsonObject);
        Document documentMapped = modelMapper.map(document, Document.class);

        documentMapped.setMetadata(jsonString);

        String hashedDocument = getHashedDocument(documentMapped);
        documentMapped.setHashedDocument(hashedDocument);
        return  documentMapped;
    }

    public static void enregistrerFichier(String dossierPath, String nomFichier, String contenu) throws IOException {
        try {
            // Créez un objet File pour le fichier
            File fichier = new File(dossierPath + File.separator + nomFichier);

            // Créez un flux de sortie pour écrire dans le fichier
            FileOutputStream fluxSortie = new FileOutputStream(fichier);

            // Convertissez le contenu en tableau de bytes
            byte[] bytes = contenu.getBytes();

            // Écrivez le contenu dans le fichier
            fluxSortie.write(bytes);

            // Fermez le flux de sortie
            fluxSortie.close();

            System.out.println("Le fichier a été enregistré avec succès dans " + fichier.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
