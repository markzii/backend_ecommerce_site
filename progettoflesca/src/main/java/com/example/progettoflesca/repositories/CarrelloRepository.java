package com.example.progettoflesca.repositories;

import com.example.progettoflesca.entities.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {

}

