package com.example.progettoflesca.exception;


public class QuantitaInsufficienteException extends Exception{

    private String nome;
    public QuantitaInsufficienteException(String nome){
        this.nome=nome;
    }

    public String getNome(){
        return nome;
    }

}
