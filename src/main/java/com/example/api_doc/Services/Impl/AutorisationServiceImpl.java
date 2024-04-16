package com.example.api_doc.Services.Impl;

import com.example.api_doc.Entities.Autorisation;
import com.example.api_doc.Exceptions.AutorisationNotAddedException;
import com.example.api_doc.Exceptions.AutorisationNotFoundException;
import com.example.api_doc.Repository.IAutorisationDAO;
import com.example.api_doc.Services.IAutorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AutorisationServiceImpl implements IAutorisationService {

    @Autowired
    private IAutorisationDAO autorisationDAO;
//LOUBNALAZGHAM
    @Override
    public Optional<Autorisation> addAutorisation(Autorisation autorisation) throws AutorisationNotAddedException {
        return autorisationDAO.addAutorisation(autorisation);
    }
    //OMARBOUKIOUD

    @Override
    public List<Autorisation> getAllAutorisations() throws AutorisationNotFoundException {
        return autorisationDAO.getAllAutorisations();
    }

//    @Override
//    public List<Autorisation> getAllAutorisations() {
//        return autorisationDAO.getAllAutorisations();
//    }

}
