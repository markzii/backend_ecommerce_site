package com.example.progettoflesca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "carrello", schema = "public")
public class Carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne(mappedBy = "carrello", optional = false)
    @JsonIgnore
    private Utente utente;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.MERGE, fetch=FetchType.LAZY)
    private List<DettaglioCarrello> prodottiCarrello = new LinkedList<>();

}
