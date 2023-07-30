package com.example.progettoflesca.exception;

import lombok.Data;

@Data
public class PrezzoCambiatoException extends Exception {
    private String nome;
    private float prezzo;
    public PrezzoCambiatoException(String nome, float p) {
        this.nome = nome;
        prezzo = p;
    }
}
