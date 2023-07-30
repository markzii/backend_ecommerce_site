package com.example.progettoflesca.controllers;

import com.example.progettoflesca.entities.Acquisto;
import com.example.progettoflesca.exception.RangeDataErratoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.progettoflesca.services.ConsultareAcquistiService;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/acquisti")
public class ConsultaAcquisti {

    @Autowired
    private ConsultareAcquistiService consultareAcquistiService;

    @GetMapping("/tutti")
    @PreAuthorize("hasAuthority('client')")
    public ResponseEntity visualizzaAcquisti(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Acquisto> risultato = consultareAcquistiService.consultareAcquisti(pageNumber, pageSize, sortBy);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }

    /*@PostMapping()
    @PreAuthorize("hasAuthority('farmacia') or hasAuthority('gestore')")
    public ResponseEntity visualizzaAcquisti(@RequestBody List<Date> date) { //1 inizio, 2 fine
        try{
            return new ResponseEntity(consultareAcquistiService.getAcquistiByUserInPeriod(date.get(0), date.get(1)), HttpStatus.OK);
        } catch (RangeDataErratoException e) {
            return new ResponseEntity("Errore: intervallo date errate", HttpStatus.OK);
        }
    }*/
}