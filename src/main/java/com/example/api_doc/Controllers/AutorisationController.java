package com.example.api_doc.Controllers;

import com.example.api_doc.Entities.Autorisation;
import com.example.api_doc.Exceptions.AutorisationNotAddedException;
import com.example.api_doc.Services.IAutorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/autorisations")
public class AutorisationController {

    @Autowired
    private IAutorisationService autorisationService;

    @PostMapping("/add")
    public Optional<Autorisation> addAutorisation(@RequestBody Autorisation autorisation) throws AutorisationNotAddedException {
        Optional<Autorisation> addedAutorisation = autorisationService.addAutorisation(autorisation);
        return addedAutorisation;

    }
}