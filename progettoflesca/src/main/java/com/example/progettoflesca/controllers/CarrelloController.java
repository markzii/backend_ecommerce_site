package com.example.progettoflesca.controllers;

import com.example.progettoflesca.entities.DettaglioCarrello;
import com.example.progettoflesca.exception.PrezzoCambiatoException;
import com.example.progettoflesca.exception.ProdottoNonEsistente;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
import com.example.progettoflesca.exception.QuantitaNegaticaException;
import com.example.progettoflesca.services.AcquistoService;
import com.example.progettoflesca.services.CarrelloService;
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

    @GetMapping("/visualizza")
    @PreAuthorize("hasAuthority('client')")
    public ResponseEntity visualizzaCarrello() {
        List<DettaglioCarrello> ret = carrelloService.visualizzaCarrello();
        return new ResponseEntity(ret,HttpStatus.OK);
    }

    @GetMapping("/aggiungi")
    @PreAuthorize("hasAuthority('client')")
    public ResponseEntity aggiungiAlCarrello(@RequestParam(value = "id", defaultValue = "-1") int id, @RequestParam(value = "quantita", defaultValue = "0") int quantita) {
        //if(codiceBarre == -1) return new ResponseEntity("Errore 'id prodotto'", HttpStatus.BAD_REQUEST);
        try{
            return new ResponseEntity(carrelloService.aggiungiAlCarrello(id,quantita),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("Quantita prodotto in magazzino insufficiente.", HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException e){ //forse non ci vuole perchè c'è la valid
            return new ResponseEntity("Errore, quantita minore di zero", HttpStatus.BAD_REQUEST);
        }
    }

    /*@PutMapping
    @PreAuthorize("hasAuthority('client')")
    //La modifica è consentita per un prodotto per volta
    public ResponseEntity modificaCarrello(@RequestParam(value = "id", defaultValue = "-1") int id, @RequestParam(value = "quantita") int quantita) { //quantita in meno o in piu
        if(id == -1) return new ResponseEntity("Errore 'id prodotto'", HttpStatus.BAD_REQUEST);

        try{
            return new ResponseEntity(carrelloService.modificaCarrello(id,quantita),HttpStatus.OK);
        }catch (QuantitaInsufficienteException e){
            return new ResponseEntity("Errorre: quantita prodotto: "+e.getIdProdotto()+" in magazzino insufficiente.", HttpStatus.BAD_REQUEST);
        }
    }*/

    @GetMapping("/aggiorna")
    @PreAuthorize("hasAuthority('client')")
    //per eliminare si usa lo stesso service di modifica con quantita = 0
    public ResponseEntity modificaCarrello(@RequestParam(value = "id", defaultValue = "-1") int id, @RequestParam(value = "quantita", defaultValue = "-1") int quantita) {
        try {
            return new ResponseEntity(carrelloService.modificaCarrello(id,quantita),HttpStatus.OK);
        } catch (ProdottoNonEsistente e) {
            return new ResponseEntity("Errore: id prodotto non presente nel carrello", HttpStatus.BAD_REQUEST);
        } catch (QuantitaNegaticaException e) {
            return new ResponseEntity("Errore: quantita negativa", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/acquisto")
    @PreAuthorize("hasAuthority('client')")
    public ResponseEntity acquistaCarrello(/*@RequestBody List<DettaglioOrdineClient> carrelloClient*/){
        int c=0;
        while(c<MAX)
            try{
                acquistoService.acquista();
                //acquistoService.acquista(carrelloClient);
                return new ResponseEntity("Acquisto avvenuto con successo", HttpStatus.OK);
            }catch(QuantitaInsufficienteException e ) {
                return new ResponseEntity("Acquisto NON avvenuto: quantita prodotto: "+e.getNome()+" in magazzino insufficiente.", HttpStatus.BAD_REQUEST);
            }catch(PrezzoCambiatoException e) {
                return new ResponseEntity("Acquisto NON avvenuto: prezzo prodotto: "+e.getNome()+" cambiato in: "+e.getPrezzo(), HttpStatus.BAD_REQUEST);
            }catch(PessimisticLockException e) {
                c++;
            }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
