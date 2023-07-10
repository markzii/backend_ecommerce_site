package com.example.progettoflesca.exception;


public class QuantitaInsufficienteException extends Exception{

    private int idProdotto;
    public QuantitaInsufficienteException(int idProdotto){
        this.idProdotto=idProdotto;
    }

    public int getIdProdotto(){
        return idProdotto;
    }

}
