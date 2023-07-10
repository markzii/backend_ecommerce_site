package com.example.progettoflesca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "dettagliocarrello", schema = "public")
public class DettaglioCarrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "quantita", nullable = true)
    private int quantita;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "acquisto")
    @JsonIgnore
    @ToString.Exclude
    private Carrello carrello;


}