package com.example.progettoflesca.repositories;

import com.example.progettoflesca.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    List<Utente> findByNome(String nome);
    List<Utente> findByCognome(String cognome);
    List<Utente> findByNomeAndCognome(String nome, String cognome);
    Utente findByEmail(String email);
    Utente findByCodiceFiscale(String codiceFiscale);
    boolean existsByEmail(String email);
    boolean existsByNomeAndCognome(String nome, String cognome);
    boolean existsBycodiceFiscale(String codiceFiscale);

    int countByNome(String nome);

    Utente save(Utente utente);
}