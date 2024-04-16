package com.example.api_doc.Repository.Impl;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Entities.User;
import com.example.api_doc.Repository.IUserDAO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.api_doc.Utils.ApiConstants.*;

@Repository
public class UserDaoImpl implements IUserDAO {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Resource(name = "userProperties")
    private Properties properties;
    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
   //LOUBNA LAZGHAM
    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(properties.getProperty("select.users"),getRowMapperUser());
    }

//omar boukioud


    @Override
    public Optional<User> getUserById(Integer userID) {
        Map<String,Object> params = new HashMap<>();
        params.put(USER_ID,userID);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(properties.getProperty("select.user.by.id"),new MapSqlParameterSource(params),getRowMapperUser()));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }



    private RowMapper<User> getRowMapperUser(){
        return (rs, rowNum) -> User.builder()
                .id(rs.getInt(USER_ID))
                .nom(rs.getString(USER_NOM))
                .email(rs.getString(USER_EMAIL))
                .build();

    }
}
