package com.example.progettoflesca.exception;


public class QuantitaInsufficienteException extends Exception{

    private String codiceBarre;
    public QuantitaInsufficienteException(String codiceBarre){
        this.codiceBarre=codiceBarre;
    }

    public String getCodiceBarre(){
        return codiceBarre;
    }

}
