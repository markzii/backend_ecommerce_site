package com.example.progettoflesca.services;

import com.example.progettoflesca.authentication.Utils;
import com.example.progettoflesca.entities.Carrello;
import com.example.progettoflesca.entities.DettaglioCarrello;
import com.example.progettoflesca.entities.Prodotto;
import com.example.progettoflesca. entities.Utente;
import com.example.progettoflesca.exception.ProdottoNonEsistente;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
import com.example.progettoflesca.exception.QuantitaNegaticaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettoflesca.repositories.DettaglioCarrelloRepository;
import com.example.progettoflesca.repositories.ProdottoRepository;
import com.example.progettoflesca.repositories.UtenteRepository;

import java.util.List;

@Service
public class CarrelloService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private DettaglioCarrelloRepository dettaglioCarrelloRepository;

    @Transactional(readOnly = true)
    public List<DettaglioCarrello> visualizzaCarrello() {
        String email = Utils.getEmail();
        Utente utente = utenteRepository.findByEmail(email);
        return utente.getCarrello().getProdottiCarrello();
    }

    @Transactional(readOnly = false, rollbackFor = {QuantitaInsufficienteException.class, IllegalArgumentException.class})
    public String aggiungiAlCarrello(int idProdotto, int quantita) throws QuantitaInsufficienteException, IllegalArgumentException{
        Prodotto prodotto = prodottoRepository.findById(idProdotto);

        if(quantita <=0 ) throw new IllegalArgumentException();
        if(prodotto.getQuantita()<quantita) throw new QuantitaInsufficienteException(prodotto.getCodiceBarre());

        boolean inCarrello = false;

        String email = Utils.getEmail();

        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello=utente.getCarrello();

        DettaglioCarrello dc = null;

        for(DettaglioCarrello pc: carrello.getProdottiCarrello()){
            if (pc.getProdotto().getId() == idProdotto) {
                pc.setQuantita(pc.getQuantita() + quantita);
                inCarrello=true;
                break;
            }
        }

        if(!inCarrello) {
            dc = new DettaglioCarrello();
            dc.setProdotto(prodotto);
            dc.setPrezzo(prodotto.getPrezzo());
            dc.setQuantita(quantita);
            dc.setCarr(carrello);
            dettaglioCarrelloRepository.save(dc);
        }
        return "true";
    }
    @Transactional(readOnly = false)
    public String modificaCarrello(int idProdotto, int quantita) throws ProdottoNonEsistente, QuantitaNegaticaException {
        if(dettaglioCarrelloRepository.existsById(idProdotto))
            throw new ProdottoNonEsistente();
        System.out.println(quantita);
        if(quantita < 0)
            throw new QuantitaNegaticaException();
        String email= Utils.getEmail();
        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello = utente.getCarrello();

        for(DettaglioCarrello pc: carrello.getProdottiCarrello()){
            if (pc.getProdotto().getId() == idProdotto) {
                if(quantita==0) { //vuoi eliminare dal carrello
                    carrello.getProdottiCarrello().remove(pc);
                    dettaglioCarrelloRepository.delete(pc);
                    return "true";
                } else if(quantita > 0 ){
                    //non ci vuole l'aggiornamento con la quantita che aumenta se ne chiede di meno perchè solo con l'acquisto si sottrae effettivamente la quantita del prodotto
                    pc.setQuantita(quantita);
                    dettaglioCarrelloRepository.save(pc);
                    return "true";
                }
            }
        }
        return "false";
    }
}