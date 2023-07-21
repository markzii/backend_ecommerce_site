package com.example.progettoflesca.services;

import lombok.Data;

@Data
public class DettaglioOrdineClient {
    private int idProd;
    private int quantita;
    private float prezzo;

    public DettaglioOrdineClient(int id, int q, float p){
        idProd = id;
        quantita = q;
        prezzo = p;
    }
}
