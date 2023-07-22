package com.example.progettoflesca.controllers;

import com.example.progettoflesca.entities.Categoria;
import com.example.progettoflesca.entities.Prodotto;
import com.example.progettoflesca.exception.*;
import com.example.progettoflesca.repositories.CategoriaRepository;
import jakarta.persistence.PessimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.progettoflesca.services.ProdottoService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/prodotti")
public class ProdottoController {
    @Autowired
    private ProdottoService prodottoService;

    private static final int MAX=5;

    @GetMapping("/getall")
    //@PreAuthorize("hasAuthority('') or hasAuthority('')")
    //@PreAuthorize("hasRole('client_user') or hasRole('client_admin')")
    public ResponseEntity getTuttiProdotti(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Prodotto> risultato = prodottoService.getTuttiProdotti(pageNumber, pageSize, sortBy);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }
    @GetMapping("/ricercaNome")
    public ResponseEntity getProdottiNome(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy, @RequestParam(value = "nome", defaultValue = "") String nome) {
        List<Prodotto> risultato = prodottoService.getProdottiDalNome(nome, pageNumber, pageSize, sortBy);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }
    @GetMapping("/ricercaCodiceBarre")
    public ResponseEntity getProdottoCodiceBarre(@RequestParam(value = "codiceBarre", defaultValue = "") String codiceBarre) {
        Prodotto risultato = prodottoService.getProdottoDalCodiceBarre(codiceBarre);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }
    @GetMapping("/ricercaCategoria")
    public ResponseEntity getProdottiCategoria(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy, @RequestParam(value = "genere", defaultValue = "") String genere) {
        List<Prodotto> risultato = prodottoService.getProdottiDallaCategoria(genere, pageNumber, pageSize, sortBy);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }
    @GetMapping("/ricercaID")
    public ResponseEntity getProdottiId(@RequestParam(value = "id") int id) {
        Prodotto risultato = prodottoService.getProdottiDaId(id);
        return new ResponseEntity(risultato, HttpStatus.OK);
    }


    /*@PostMapping("/addc")
    //@PreAuthorize("hasRole('client_user')")
    public ResponseEntity aggiungiCategoria(@RequestBody  Categoria c) {
        System.out.println("comeee");
        Categoria risultato = prodottoService.aggiungiCategoria(c);
        return new ResponseEntity(risultato,HttpStatus.OK);
    }*/

    /*@PostMapping("/addp")
    //@PreAuthorize("hasRole('client_user')")
    public ResponseEntity aggiungiProdotto(@RequestBody  Prodotto prodotto) {
        System.out.println("ciaooooo");
        try {
            Prodotto risultato = prodottoService.aggiungiProdotto(prodotto);

            //Prodotto risultato = prodottoService.aggiungiProdotto(prodotto.getCodiceBarre(), prodotto.getNome(),prodotto.getPrezzo(), prodotto.getQuantita(),prodotto.getDescrizione(),prodotto.getCategoria().getNome());
            return new ResponseEntity(risultato,HttpStatus.OK);
        }catch(ProdottoEsistenteException e) {
            //si potrebbe attivare le cose delle lingue
            return new ResponseEntity("Prodotto gia esistente", HttpStatus.BAD_REQUEST);
        } catch (NoCodiceBarreException e) {
            return new ResponseEntity("Inserire codice a barre", HttpStatus.BAD_REQUEST);
        }
    }*/
    /*@PutMapping("/{id}/aggiorna")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity modificaProdotto( @RequestParam(value = "id") int id, @RequestParam(value = "quantita", defaultValue = "0") int quantita, @RequestParam(value = "prezzo", defaultValue = "0") float prezzo){
        int c=1;

        //siccome ho usato il lock pessimistico devo vedere se va a buon fine, non alla prima volta mi viene dato
        while (c<=MAX) {
            try {
                return new ResponseEntity(prodottoService.aggiornaProdotto(id, quantita, prezzo), HttpStatus.OK);
            } catch (AggiornamentoErroreException e) {
                return new ResponseEntity("Errore aggiornamento, controlla i valori forniti", HttpStatus.BAD_REQUEST);
            } catch (PessimisticLockException e) {
                c++;
            }
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    /*@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity eliminaProdotto( @PathVariable("id") int id){
        int c=1;
        //siccome ho usato il lock pessimistico devo vedere se va a buon fine, non alla prima volta mi viene dato
        while (c<=MAX) {
            try {
                prodottoService.eliminaProdotto(id);
                return new ResponseEntity("Eliminato con successo", HttpStatus.OK);
            } catch (EliminazioneErroreException e) {
                return new ResponseEntity("Errore: id prodotto errato", HttpStatus.BAD_REQUEST);
            } catch (PessimisticLockException e) {
                c++;
            }
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
