package com.example.progettoflesca.repositories;

import com.example.progettoflesca.entities.Acquisto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AcquistoRepository extends JpaRepository<Acquisto, Integer> {
    @Query("select a from Acquisto a where a.utente.email=:email")
    Page<Acquisto> findByEmail(String email, Pageable pageable);

    /*@Query("select p from Acquisto p where p.oraacquisto > ?1 and p.oraacquisto < ?2 and p.utente.email = ?3")
    List<Acquisto> findByAcquirenteInData(Date startDate, Date endDate, String email);*/
}
