    package com.example.api_doc.Repository.Impl;

    import com.example.api_doc.Entities.Document;
    import com.example.api_doc.Exceptions.DocumentNotAddedException;
    import com.example.api_doc.Exceptions.DocumentNotDeletedException;
    import com.example.api_doc.Exceptions.DocumentUpdateFailException;
    import com.example.api_doc.Repository.IDocumentDAO;
    import jakarta.annotation.Resource;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.dao.EmptyResultDataAccessException;
    import org.springframework.data.jdbc.repository.query.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
    import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
    import org.springframework.jdbc.support.GeneratedKeyHolder;
    import org.springframework.jdbc.support.KeyHolder;
    import org.springframework.stereotype.Repository;
    import org.springframework.web.multipart.MultipartFile;

    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.attribute.BasicFileAttributeView;
    import java.nio.file.attribute.FileTime;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.*;

    import static com.example.api_doc.Utils.ApiConstants.*;

    @Repository
    public class DocumentDaoImpl   implements IDocumentDAO  {

        @Autowired
        final NamedParameterJdbcTemplate jdbcTemplate;

        @Value("${dossier.chemin}")
        private String dossierPath;
        @Resource(name = "documentProperties")
        private Properties properties;

        public DocumentDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public Optional<Document> addDocument(Document document,MultipartFile file) throws DocumentNotAddedException {
            try {
                document.setNomDocument(file.getOriginalFilename());
                document.setTypeDocument(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
                Path filePath = Paths.get(dossierPath, document.getNomDocument());
                BasicFileAttributeView attributeView = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
                FileTime fileTime = attributeView.readAttributes().creationTime();
                LocalDateTime creationDateTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
//                document.setDateCreation(LocalDateTime.now());
                document.setDateCreation(creationDateTime);
                document.setLinkDocument("http://localhost:8081/documents/"+document.getNomDocument());






                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(properties.getProperty("insert.document"),getSqlParameterSourceDocument(document),keyHolder);
                document.setUuid(Objects.requireNonNull(keyHolder.getKey()).intValue());
                return Optional.of(document);
            }catch (Exception e) {
                throw (DocumentNotAddedException) new DocumentNotAddedException("Document non ajouté! ").initCause(e);
            }

        }

        @Override
        public List<Document> getAllDocument() {
            return jdbcTemplate.query(properties.getProperty("select.documents"),getRowMapperDocument());
        }

        @Override
        public Optional<Document> getDocumentById(Integer DocumentID) {
            Map<String,Object> params = new HashMap<>();
            params.put(DOCUMENT_ID,DocumentID);
            try {
                return Optional.ofNullable(jdbcTemplate.queryForObject(properties.getProperty("select.document.by.uuid"),new MapSqlParameterSource(params),getRowMapperDocument()));
            }catch (EmptyResultDataAccessException e){
                return Optional.empty();
            }

        }

        @Override
        public Optional<Document> getDocumentByHashedDocument(String hashedDocument) {
            Map<String,Object> params = new HashMap<>();
            params.put(DOCUMENT_HASHED_DOCUMENT,hashedDocument);
            try {
                return Optional.ofNullable(jdbcTemplate.queryForObject(properties.getProperty("select.document.by.hashedDocument"),new MapSqlParameterSource(params),getRowMapperDocument()));
            }catch (EmptyResultDataAccessException e){
                return Optional.empty();
            }
        }

        @Override
        public Integer deleteDocument(Integer DocumentID) throws DocumentNotDeletedException {
            Map<String,Object> params = new HashMap<>();
            params.put(DOCUMENT_ID,DocumentID);
            int deletedDocument = jdbcTemplate.update(properties.getProperty("delete.document.by.uuid"),new MapSqlParameterSource(params));
            if(deletedDocument == 0){
                throw new DocumentNotDeletedException("Document non supprimé!");
            }
            return deletedDocument;
        }

        @Override
        public Optional<Document> updateDocument(Document document) throws DocumentUpdateFailException {
            try {
                int updated = jdbcTemplate.update(properties.getProperty("update.document"), getSqlParameterSourceDocument(document));
                if (updated != 0) {
                    return Optional.of(document);
                } else {
                    throw new DocumentUpdateFailException("Document non modifié! ");
                }
            }catch (Exception e) {
                throw (DocumentUpdateFailException) new DocumentUpdateFailException(e.getMessage()).initCause(e);
            }
        }

        @Override
        public List<Document> getDocumentByNom(String nom) {
            Map<String,Object> params = new HashMap<>();
            params.put(DOCUMENT_NOM,nom);
            try {
                return jdbcTemplate.query(properties.getProperty("select.document.by.nom"),new MapSqlParameterSource(params),getRowMapperDocument());
            }catch (EmptyResultDataAccessException e){
                return Collections.emptyList();
            }
        }

        @Override
        public List<Document> getDocumentByDateCreation(Date date) {
            Map<String,Object> params = new HashMap<>();
            params.put(DOCUMENT_DATE_CREATION,date);
            try {
                return jdbcTemplate.query(properties.getProperty("select.document.by.date"),new MapSqlParameterSource(params),getRowMapperDocument());
            }catch (EmptyResultDataAccessException e){
                return Collections.emptyList();
            }
        }

        @Override
        public List<Document> getDocumentByType(String type) {
            Map<String,Object> params = new HashMap<>();
            params.put(DOCUMENT_TYPE,type);
            try {
                return jdbcTemplate.query(properties.getProperty("select.document.by.type"),new MapSqlParameterSource(params),getRowMapperDocument());
            }catch (EmptyResultDataAccessException e){
                return Collections.emptyList();
            }
        }



        private MapSqlParameterSource getSqlParameterSourceDocument(Document document){
            return new MapSqlParameterSource()
                    .addValue(DOCUMENT_ID,document.getUuid())
                    .addValue(DOCUMENT_NOM,document.getNomDocument())
                    .addValue(DOCUMENT_TYPE,document.getTypeDocument())
                    .addValue(DOCUMENT_DATE_CREATION,document.getDateCreation())
                    .addValue(DOCUMENT_METADATA,document.getMetadata())
                    .addValue(DOCUMENT_LINK,document.getLinkDocument())
                    .addValue(DOCUMENT_HASHED_DOCUMENT,document.getHashedDocument());
        }

        private RowMapper<Document> getRowMapperDocument(){
            return (rs, rowNum) -> Document.builder()
                    .uuid(rs.getInt(DOCUMENT_ID))
                    .nomDocument(rs.getString(DOCUMENT_NOM))
                    .typeDocument(rs.getString(DOCUMENT_TYPE))
                    .dateCreation(rs.getTimestamp(DOCUMENT_DATE_CREATION).toLocalDateTime())
                    .metadata(rs.getString(DOCUMENT_METADATA))
                    .hashedDocument(rs.getString(DOCUMENT_HASHED_DOCUMENT))
                    .linkDocument(rs.getString(DOCUMENT_LINK))
                    .build();

        }


    }
