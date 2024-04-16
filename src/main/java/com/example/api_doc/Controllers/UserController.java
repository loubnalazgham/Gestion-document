package com.example.api_doc.Controllers;


import com.example.api_doc.Entities.Document;
import com.example.api_doc.Entities.User;
import com.example.api_doc.Services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
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
