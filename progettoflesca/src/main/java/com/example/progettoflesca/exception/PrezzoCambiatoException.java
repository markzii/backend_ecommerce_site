package com.example.progettoflesca.exception;

import lombok.Data;

@Data
public class PrezzoCambiatoException extends Throwable {
    private int idProdotto;
    private float prezzo;
    public PrezzoCambiatoException(int id, float p) {
        idProdotto = id;
        prezzo = p;
    }
}
