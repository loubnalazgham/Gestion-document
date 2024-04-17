package com.example.api_doc.Repository.Impl;

import com.example.api_doc.Entities.Autorisation;
import com.example.api_doc.Entities.Document;
import com.example.api_doc.Entities.User;
import com.example.api_doc.Exceptions.AutorisationNotAddedException;
import com.example.api_doc.Repository.IAutorisationDAO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static com.example.api_doc.Utils.ApiConstants.*;
import static com.example.api_doc.Utils.ApiConstants.DOCUMENT_LINK;

@Repository
public class AutorisationDaoImpl implements IAutorisationDAO {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Resource(name = "autorisationProperties")
    private Properties properties;
    @Override
    public Optional<Autorisation> addAutorisation(Autorisation autorisation) throws AutorisationNotAddedException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (0 != jdbcTemplate.update(properties.getProperty("insert.autorisation"), getSqlParameterSourceAutorisation(autorisation), keyHolder)) {
            if(keyHolder.getKey()!=null) {
                autorisation.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            }
            return Optional.of(autorisation);
        }else {
            throw  new AutorisationNotAddedException("Problème d'ajout d'autorisation");
        }
    }

    @Override
    public List<Autorisation> getAllAutorisations() {
        return null;
    }

    private MapSqlParameterSource getSqlParameterSourceAutorisation(Autorisation autorisation){
        return new MapSqlParameterSource()
                .addValue(AUTORISATION_ID,autorisation.getId())
                .addValue(AUTORISATION_TYPE_AUTORISATION,autorisation.getTypeAutorisation())
                .addValue(AUTORISATION_DOCUMENT_ID,autorisation.getDocument().getUuid())
                .addValue(AUTORISATION_USER_ID,autorisation.getUser().getId());
    }

    private RowMapper<Autorisation> getRowMapperAutorisation(){
        return (rs, rowNum) -> Autorisation.builder()
                .id(rs.getInt(AUTORISATION_ID))
                .user(User.builder()
                        .id(rs.getInt(AUTORISATION_USER_ID))
                        .build())
                .document(Document.builder()
                        .uuid(rs.getInt(AUTORISATION_DOCUMENT_ID))
                        .build())
                .build();

    }

}
