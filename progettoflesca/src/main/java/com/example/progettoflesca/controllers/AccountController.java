package com.example.progettoflesca.controllers;


import com.example.progettoflesca.configurations.KeyCloak;
import com.example.progettoflesca.entities.DTOUtente;
import com.example.progettoflesca.entities.Utente;
import com.example.progettoflesca.exception.MailUsataEsisteException;
import com.example.progettoflesca.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/registrazione")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity create(@RequestBody DTOUtente utente) {
        try {
            Utente aggiunto = accountService.registerUser(utente);
            return new ResponseEntity(aggiunto, HttpStatus.OK);
        } catch (MailUsataEsisteException e) {
            return new ResponseEntity("La email usata esiste", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Errore da keycloak", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Utente> getAll() {
        return accountService.getAllUsers();
    }


}
