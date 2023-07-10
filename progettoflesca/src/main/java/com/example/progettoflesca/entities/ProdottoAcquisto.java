package com.example.progettoflesca.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "prodottoacquisto", schema = "public")
public class ProdottoAcquisto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "quantita", nullable = true)
    private int quantita;

    @Basic
    @Column(name = "prezzo", nullable = true)
    private float prezzo;

    @ManyToOne
    @JoinColumn(name = "acquisto")
    @JsonIgnore
    @ToString.Exclude
    private Acquisto acquisto;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;
}