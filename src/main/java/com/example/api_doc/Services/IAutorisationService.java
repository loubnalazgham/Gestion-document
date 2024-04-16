package com.example.api_doc.Services;

import com.example.api_doc.Entities.Autorisation;
import com.example.api_doc.Exceptions.AutorisationNotAddedException;
import com.example.api_doc.Exceptions.AutorisationNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IAutorisationService {
    Optional<Autorisation> addAutorisation(Autorisation autorisation) throws AutorisationNotAddedException;

    List<Autorisation> getAllAutorisations() throws AutorisationNotFoundException;
}
