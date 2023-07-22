package com.example.progettoflesca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "dettagliocarrello", schema = "public")
public class DettaglioCarrello {
    @EqualsAndHashCode.Exclude
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

    @ManyToOne(cascade = CascadeType.MERGE)//il cascade tipe qui potrebbe essere pericoloso, perchè propaga le modifiche sui prodotti visti da tutti gli utenti?
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "carrello")
    @JsonIgnore
    @ToString.Exclude
    private Carrello carr;


}