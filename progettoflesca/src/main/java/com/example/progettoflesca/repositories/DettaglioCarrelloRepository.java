package com.example.progettoflesca.repositories;

import com.example.progettoflesca.entities.DettaglioCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DettaglioCarrelloRepository extends JpaRepository<DettaglioCarrello, Integer> {

    boolean existsById (int id);


}