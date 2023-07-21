package com.example.progettoflesca.services;


import com.example.progettoflesca.configurations.KeyCloak;
import com.example.progettoflesca.entities.Carrello;
import com.example.progettoflesca.entities.DTOUtente;
import com.example.progettoflesca.entities.Utente;
import com.example.progettoflesca.exception.MailUsataEsisteException;
import com.example.progettoflesca.repositories.CarrelloRepository;
import com.example.progettoflesca.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AccountService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Utente registerUser(DTOUtente dtoUtente) throws MailUsataEsisteException, Exception{
        if ( utenteRepository.existsByEmail(dtoUtente.getEmail()) ) {
            throw new MailUsataEsisteException();
        }
        System.out.println(dtoUtente);
        KeyCloak.aggiungiUtenteAKeycloack(dtoUtente.getNome(), dtoUtente.getEmail(), dtoUtente.getPassword());
        Utente utente = dtoUtente.convertToUser();
        System.out.println(utente);
        Carrello carrello = new Carrello();
        carrelloRepository.save(carrello);
        utente.setCarrello(carrello);
        return utenteRepository.save(utente);
    }

    @Transactional(readOnly = true)
    public List<Utente> getAllUsers() {
        return utenteRepository.findAll();
    }


}
