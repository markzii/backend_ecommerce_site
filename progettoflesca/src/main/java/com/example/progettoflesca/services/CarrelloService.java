package com.example.progettoflesca.services;

import com.example.progettoflesca.authentication.Utils;
import com.example.progettoflesca.entities.Carrello;
import com.example.progettoflesca.entities.DettaglioCarrello;
import com.example.progettoflesca.entities.Prodotto;
import com.example.progettoflesca. entities.Utente;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
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

    @Transactional(rollbackFor = {QuantitaInsufficienteException.class, IllegalArgumentException.class})
    public Carrello aggiungiAlCarrello(int idProdotto, int quantita) throws QuantitaInsufficienteException, IllegalArgumentException{
        //Prodotto prodotto = prodottoRepository.findByIdWithLock(idProdotto, LockModeType.OPTIMISTIC);
        System.out.println(idProdotto);
        System.out.println(quantita);
        Prodotto prodotto = prodottoRepository.findById(idProdotto);
        //System.out.println(prodotto);
        if(quantita <=0 ) throw new IllegalArgumentException();
        if(prodotto.getQuantita()<quantita) throw new QuantitaInsufficienteException(prodotto.getId());

        boolean inCarrello = false;

        String email = Utils.getEmail();

        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello=utente.getCarrello();

        DettaglioCarrello dc = null;

        for(DettaglioCarrello pc: carrello.getProdottiCarrello()){
            if (pc.getProdotto().getId() == idProdotto) {
                pc.setQuantita(dc.getQuantita() + quantita);
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
        int q = prodotto.getQuantita();
        //prodotto.setQuantita(q-quantita);
        //prodottoRepository.save(prodotto);
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
                } else
                    if(quantita > 0 && pc.getProdotto().getQuantita() < quantita)
                        throw new QuantitaInsufficienteException(pc.getProdotto().getId());
                    else {
                        //non ci vuole l'aggiornamento con la quantita che aumenta se ne chiede di meno perchè solo con l'acquisto si sottrae effettivamente la quantita del prodotto
                        pc.setQuantita(quantita);
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