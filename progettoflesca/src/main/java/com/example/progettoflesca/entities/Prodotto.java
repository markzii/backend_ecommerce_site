package com.example.progettoflesca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "prodotto", schema = "public")
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "codiceBarre", nullable = false, length = 70)
    private String codiceBarre;

    @Column(name = "prezzo", nullable = false)
    private float prezzo;

    @Column(name = "quantita", nullable = false)
    private int quantita;

    @Column(name = "descrizione", nullable = true, length = 500)
    private String descrizione;

    @JoinColumn(name = "categoria")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Categoria categoria;

    @Version
    @Column(name = "versione", nullable = false)
    @JsonIgnore
    private long versione;


    @OneToMany(targetEntity = DettaglioCarrello.class, mappedBy = "prodotto", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<DettaglioCarrello> prodottiAcquistati;


}
