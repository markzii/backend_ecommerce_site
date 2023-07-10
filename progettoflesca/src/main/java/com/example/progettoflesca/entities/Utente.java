package com.example.progettoflesca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "utente", schema = "public")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "codiceFiscale", nullable = true, length = 15)
    private String codiceFiscale;

    @Column(name = "nome", nullable = true, length = 50)
    private String nome;

    @Column(name = "cognome", nullable = true, length = 50)
    private String cognome;

    @Column(name = "email", nullable = true, length = 60)
    private String email;

    @Column(name = "telefono", nullable = true, length = 10)
    private String numeroTelefono;

    @Column(name = "indirizzo", nullable = true, length = 150)
    private String indirizzo;

    //@OneToOne(mappedBy = "utente", cascade = CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "carrello")
    private Carrello carrello;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Acquisto> acquisti;

}
