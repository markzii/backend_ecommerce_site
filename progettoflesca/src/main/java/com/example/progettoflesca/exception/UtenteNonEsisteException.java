package com.example.progettoflesca.exception;

import lombok.Data;

@Data
public class UtenteNonEsisteException extends Exception {
    private String email;
    public UtenteNonEsisteException(String email) {
        this.email = email;
    }
}
