package com.example.progettoflesca.repositories;

import com.example.progettoflesca.entities.Prodotto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {
    List<Prodotto> findByNome(String nome);
    Page<Prodotto> findByNome(String nome, Pageable pageable);
    boolean existsBycodiceBarre (String codiceBarre);
    Prodotto findBycodiceBarre(String codiceBarre);
    List<Prodotto> findBycategoria(String categoria);
    Page<Prodotto> findBycategoria(String categoria, Pageable pageable);
    List<Prodotto> findAll();

    //Prodotto findByIdWithLock(int id, LockModeType pessimisticWrite);
    Prodotto findById(int id);

}
