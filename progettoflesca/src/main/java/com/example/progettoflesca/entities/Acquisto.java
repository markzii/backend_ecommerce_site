package com.example.progettoflesca.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "acquisto", schema = "public")
public class Acquisto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;

    /*@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "oraAcquisto")
    private Date purchaseTime;*/

    @ManyToOne
    @JoinColumn(name = "acquirente")
    private Utente utente;

    @OneToMany(mappedBy = "acquisto", cascade = CascadeType.MERGE)
    private List<ProdottoAcquisto> prodottoAcquisto;
}
