package com.example.api_doc.Controllers;


import com.example.api_doc.Entities.Document;
import com.example.api_doc.Entities.User;
import com.example.api_doc.Services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    IUserService iUserService;
    @GetMapping
    public List<User> getAllUsers() {
        return iUserService.getAllUsers();
    }

    @GetMapping("/{userID}")
    public Optional<User> getUserById(@PathVariable Integer userID) {
        return iUserService.getUserById(userID);
    }
}
