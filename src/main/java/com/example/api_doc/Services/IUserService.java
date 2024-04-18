package com.example.api_doc.Services;

import com.example.api_doc.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Integer userID);
}
