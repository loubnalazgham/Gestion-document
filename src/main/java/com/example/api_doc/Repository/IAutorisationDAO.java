package com.example.api_doc.Repository;

import com.example.api_doc.Entities.Autorisation;
import com.example.api_doc.Entities.Document;
import com.example.api_doc.Exceptions.AutorisationNotAddedException;
import com.example.api_doc.Exceptions.AutorisationNotFoundException;
import com.example.api_doc.Exceptions.DocumentNotAddedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IAutorisationDAO {

    Optional<Autorisation> addAutorisation(Autorisation autorisation) throws AutorisationNotAddedException;
    List<Autorisation> getAllAutorisations() throws AutorisationNotFoundException;
}
