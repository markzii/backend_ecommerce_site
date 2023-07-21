package com.example.progettoflesca.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DTOUtente {
    private int id;
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private String email;
    private String numeroTelefonico;
    private String indirizzo;
    private String password;

    public Utente convertToUser(){
        Utente res= new Utente();
        res.setCognome(cognome);
        res.setNome(nome);
        res.setEmail(email);
        res.setIndirizzo(indirizzo);
        res.setNumeroTelefono(numeroTelefonico);
        res.setCodiceFiscale(codiceFiscale);
        res.setAcquisti(new LinkedList<>());
        return res;
    }
}
