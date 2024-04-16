package com.example.api_doc.Controllers;

import com.example.api_doc.Entities.Autorisation;
import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.AutorisationNotAddedException;
import com.example.api_doc.Exceptions.AutorisationNotFoundException;
import com.example.api_doc.Services.IAutorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/autorisations")

public class AutorisationController {

    @Autowired
    private IAutorisationService autorisationService;
//loubna lazgham
    @PostMapping
    public Optional<Autorisation> addAutorisation(@RequestBody Autorisation autorisation) throws AutorisationNotAddedException {
        Optional<Autorisation> addedAutorisation = autorisationService.addAutorisation(autorisation);
        return addedAutorisation;

    }
//OMAR BOUKIOUD
    @GetMapping
    public List<Autorisation> getAllDocument() throws AutorisationNotFoundException {
        return autorisationService.getAllAutorisations();
    }
}