package com.example.api_doc.Services.Impl;

import com.example.api_doc.Entities.User;
import com.example.api_doc.Repository.IUserDAO;
import com.example.api_doc.Services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
//loubna lazgham
    @Autowired
    IUserDAO iUserDAO;
    @Override
    public List<User> getAllUsers() {
        return iUserDAO.getAllUsers();
    }

    @Override
    public Optional<User> getUserById(Integer userID) {
        return iUserDAO.getUserById(userID);
    }
}
