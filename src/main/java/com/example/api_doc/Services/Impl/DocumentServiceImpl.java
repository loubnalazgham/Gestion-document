package com.example.api_doc.Services.Impl;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.DocumentNotAddedException;
import com.example.api_doc.Exceptions.DocumentNotDeletedException;
import com.example.api_doc.Exceptions.DocumentUpdateFailException;
import com.example.api_doc.Repository.IDocumentDAO;
import com.example.api_doc.Requests.Attribute;
import com.example.api_doc.Requests.DocumentRequest;
import com.example.api_doc.Requests.MetaData;
import com.example.api_doc.Services.IDocumentService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements IDocumentService {



    @Value("${dossier.chemin}")
    private String dossierPath;

    @Autowired
    private IDocumentDAO iDocumentDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public DocumentServiceImpl(IDocumentDAO repository) {
        this.iDocumentDAO = repository;
    }


    @Override
    public Optional<Document> addDocument(MultipartFile file) throws DocumentNotAddedException, IOException, NoSuchAlgorithmException {
        DocumentRequest document = new DocumentRequest();
        MetaData metaData  = new MetaData();
        metaData.setAttributes(List.of(new Attribute("auteur","younss"),new Attribute("size","14 bits")));
        document.setMetadataSup(metaData);
        Document documentMapped = getDocumentMapped(document);
        String hashFile = getHashFile(file);
        documentMapped.setHashedDocument(hashFile);
        Optional<Document> documentByHashedDocument = getDocumentByHashedDocument(documentMapped.getHashedDocument());
        if (documentByHashedDocument.isPresent()) {
            throw new DocumentNotAddedException("Document existe deja avec ce code de hachage! ");
        }else {
//            String nomFichier = documentMapped.getNomDocument()+"."+documentMapped.getTypeDocument();
//            String contenuFichier = documentMapped.getMetadata().replace('{', ' ').replace('}', ' ').replace(',', '\n');
//
//            enregistrerFichier(dossierPath, nomFichier, contenuFichier);
            return iDocumentDAO.addDocument(documentMapped,file);
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




    public String getHashedDocument(Document document) throws NoSuchAlgorithmException {
        // Créez un objet MessageDigest avec l'algorithme de hachage souhaité (SHA-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Exemple de métadonnées (à remplacer par vos propres données)
        String hashedDocument = document.getMetadata();
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

//        String hashedDocument = getHashedDocument(documentMapped);
//        documentMapped.setHashedDocument(hashedDocument);
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
    private RowMapper<Document> rowMapper = (rs, rowNum) -> {
        Document document = new Document();
        document.setUuid(rs.getInt("UUID"));
        document.setNomDocument(rs.getString("NOM_DOCUMENT"));
        document.setTypeDocument(rs.getString("TYPE_DOCUMENT"));
        document.setLinkDocument(rs.getString("LINK_DOCUMENT"));
        java.sql.Date dateCreation = rs.getDate("DATE_CREATION");
        if (dateCreation != null) {
            LocalDateTime localDateTime = dateCreation.toLocalDate().atStartOfDay();
            document.setDateCreation(localDateTime);
        }        document.setMetadata(rs.getString("METADATA"));
        document.setHashedDocument(rs.getString("HASHED_DOCUMENT"));

        return document;
    };

    public List<Document> searchDocuments(String nom, String type,Date date_de_creation) {
        StringBuilder sb = new StringBuilder("SELECT * FROM Document WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (nom != null) {
            sb.append("AND NOM_DOCUMENT LIKE ?");
            params.add("%" + nom + "%");
        }
        if (type != null) {
            sb.append("AND TYPE_DOCUMENT = ?");
            params.add(type);
        }
        if (date_de_creation != null) {
            sb.append("AND DATE_CREATION = ?");
            Instant instant = date_de_creation.toInstant();
            // Créer un ZonedDateTime à partir de l'Instant
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            // Extraire LocalDateTime à partir de ZonedDateTime
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            params.add(localDateTime);
        }

        return jdbcTemplate.query(sb.toString(), rowMapper, params.toArray());
    }

    public String getHashFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        try (InputStream inputStream = file.getInputStream()) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
