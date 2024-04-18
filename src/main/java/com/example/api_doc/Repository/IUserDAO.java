package com.example.api_doc.Repository;

import com.example.api_doc.Entities.Document;
import com.example.api_doc.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {


    List<User> getAllUsers();

    Optional<User> getUserById(Integer userID);
}
