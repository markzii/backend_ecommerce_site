package com.example.progettoflesca.controllers;

import com.example.progettoflesca.entities.Carrello;
import com.example.progettoflesca.exception.PrezzoCambiatoException;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
import com.example.progettoflesca.service.AcquistoService;
import com.example.progettoflesca.service.CarrelloService;
import com.example.progettoflesca.service.DettaglioOrdineClient;
import jakarta.persistence.PessimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private AcquistoService acquistoService;

    private static final int MAX=5;

    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity visualizzaCarrello() {
        Carrello ret = carrelloService.visualizzaCarrello();
        return new ResponseEntity(ret,HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity aggiungiAlCarrello(@RequestParam(value = "id", defaultValue = "-1") int id, @RequestParam(value = "quantita", defaultValue = "0") int quantita) {
        if(id == -1) return new ResponseEntity("Errore 'id prodotto'", HttpStatus.BAD_REQUEST);
        try{
            return new ResponseEntity(carrelloService.aggiungiAlCarrello(id,quantita),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("Quantita prodotto: "+e.getIdProdotto()+" in magazzino insufficiente.", HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException e){ //forse non ci vuole perchè c'è la valid
            return new ResponseEntity("Errore, quantita minore di zero", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('client_user')")
    //La modifica è consentita per un prodotto per volta
    public ResponseEntity modificaCarrello(@RequestParam(value = "id", defaultValue = "-1") int id, @RequestParam(value = "quantita") int quantita) { //quantita in meno o in piu
        if(id == -1) return new ResponseEntity("Errore 'id prodotto'", HttpStatus.BAD_REQUEST);

        try{
            return new ResponseEntity(carrelloService.modificaCarrello(id,quantita),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("Errorre: quantita prodotto: "+e.getIdProdotto()+" in magazzino insufficiente.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('client_user')")
    //per eliminare si usa lo stesso service di modifica con quantita = 0
    public ResponseEntity eliminaProdottoInCarrello(@RequestParam(value = "id", defaultValue = "-1") int id) {
        if(id == -1) return new ResponseEntity("Errore 'id prodotto'", HttpStatus.BAD_REQUEST);

        try{
            return new ResponseEntity(carrelloService.modificaCarrello(id,0),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){ //non verrà mai generata
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/acquisto")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity acquistaCarrello(@RequestBody List<DettaglioOrdineClient> carrelloClient){
        int c=0;
        while(c<MAX)
            try{
                acquistoService.acquista(carrelloClient);
                return new ResponseEntity("Acquista avvenuto con successo", HttpStatus.OK);
            }catch(QuantitaInsufficienteException e ) {
                return new ResponseEntity("Errorre: quantita prodotto: "+e.getIdProdotto()+" in magazzino insufficiente.", HttpStatus.BAD_REQUEST);
            }catch(PrezzoCambiatoException e) {
                return new ResponseEntity("Errorre: prezzo prodotto: "+e.getIdProdotto()+" cambiato in: "+e.getPrezzo(), HttpStatus.BAD_REQUEST);
            }catch(PessimisticLockException e) {
                c++;
            }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
