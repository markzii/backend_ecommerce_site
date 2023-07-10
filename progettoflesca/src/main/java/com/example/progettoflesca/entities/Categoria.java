package com.example.progettoflesca.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "categoria", schema = "public")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "nome", nullable = false, length = 40)
    private String nome;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Prodotto> prodotti = new LinkedList<>();

}
