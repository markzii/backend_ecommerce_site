package com.example.progettoflesca.service;

import com.example.progettoflesca.authentication.Utils;
import com.example.progettoflesca.entities.Carrello;
import com.example.progettoflesca.entities.DettaglioCarrello;
import com.example.progettoflesca.entities.Prodotto;
import com.example.progettoflesca. entities.Utente;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettoflesca.repositories.DettaglioCarrelloRepository;
import com.example.progettoflesca.repositories.ProdottoRepository;
import com.example.progettoflesca.repositories.UtenteRepository;
@Service
public class CarrelloService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private DettaglioCarrelloRepository dettaglioCarrelloRepository;

    @Transactional(readOnly = true)
    public Carrello visualizzaCarrello() {
        String email = Utils.getEmail();
        Utente utente = utenteRepository.findByEmail(email);
        return utente.getCarrello();
    }

    @Transactional(rollbackFor = {QuantitaInsufficienteException.class, IllegalArgumentException.class})
    public Carrello aggiungiAlCarrello(int idProdotto, int quantita) throws QuantitaInsufficienteException, IllegalArgumentException{
        //Prodotto prodotto = prodottoRepository.findByIdWithLock(idProdotto, LockModeType.OPTIMISTIC);
        Prodotto prodotto = prodottoRepository.findById(idProdotto);

        if(quantita <=0 ) throw new IllegalArgumentException();
        if(prodotto.getQuantita()<quantita) throw new QuantitaInsufficienteException(prodotto.getId());

        boolean inCarrello = false;

        String email = Utils.getEmail();

        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello=utente.getCarrello();

        DettaglioCarrello dc = null;

        for(DettaglioCarrello pc: carrello.getProdottiCarrello()){
            if (dc.getProdotto().getId() == idProdotto) {
                dc.setQuantita(dc.getQuantita() + quantita);
                inCarrello=true;
                break;
            }
        }

        if(!inCarrello) {
            dc = new DettaglioCarrello();
            dc.setProdotto(prodotto);
            dc.setQuantita(quantita);
            carrello.getProdottiCarrello().add(dc);
            dettaglioCarrelloRepository.save(dc);
        }
        return carrello;
    }
    @Transactional(rollbackFor = QuantitaInsufficienteException.class)
    public Carrello modificaCarrello(int idProdotto, int quantita) throws QuantitaInsufficienteException{

        String email= Utils.getEmail();
        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello = utente.getCarrello();


        for(DettaglioCarrello pc: carrello.getProdottiCarrello()){
            if (pc.getProdotto().getId() == idProdotto) {
                if(quantita==0) { //vuoi eliminare dal carrello
                    pc.setQuantita(quantita);
                    carrello.getProdottiCarrello().remove(pc);
                    dettaglioCarrelloRepository.delete(pc);
                    break;
                } else if(quantita > 0 && pc.getProdotto().getQuantita() < pc.getQuantita()+quantita)
                    throw new QuantitaInsufficienteException(pc.getProdotto().getId());
                else {
                    pc.setQuantita(pc.getQuantita() + quantita);
                    dettaglioCarrelloRepository.save(pc);
                    break;
                }
            }
        }

        /*if(dc.getQuantita()==0) {
            carrello.getProdottiCarrello().remove(dc);
            dettaglioCarrelloRepository.delete(dc);
        }
        dettaglioCarrelloRepository.save(dc);*/
        return carrello;
    }
}