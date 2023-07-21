package com.example.progettoflesca.repositories;

import com.example.progettoflesca.entities.Categoria;
import com.example.progettoflesca.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Categoria findByNome(String nome);

    boolean existsByNome(String nome);
}
