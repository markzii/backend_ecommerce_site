package com.example.progettoflesca.services;

import com.example.progettoflesca.authentication.Utils;
import com.example.progettoflesca.entities.*;
import com.example.progettoflesca.exception.PrezzoCambiatoException;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
import com.example.progettoflesca.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcquistoService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private AcquistoRepository acquistoRepository;
    @Autowired
    private ProdottoAcquistoRepository prodottoAcquistoRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;

    @Transactional(readOnly = false, rollbackFor = {QuantitaInsufficienteException.class, PrezzoCambiatoException.class})
    public void acquista() throws QuantitaInsufficienteException, PrezzoCambiatoException {
        String email= Utils.getEmail();

        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello = utente.getCarrello();

        List<DettaglioCarrello> listaProdotti = carrello.getProdottiCarrello();

        for(DettaglioCarrello prod: listaProdotti) {
            Prodotto prodotto = prodottoRepository.findById(prod.getProdotto().getId()); //prendo il prodotto con LOCK perchè mentre si acquista nessuno deve poterlo modificare

            if(prod.getProdotto().getId() == prodotto.getId()) {//verifico del prezzo se è cambiato e se si ha il numero di pezzi
                if (prod.getPrezzo() != prodotto.getPrezzo())
                    throw new PrezzoCambiatoException(prodotto.getNome(), prodotto.getPrezzo());
                if (prodotto.getQuantita() < prod.getQuantita())
                    throw new QuantitaInsufficienteException(prodotto.getNome());
            }
        }

        Acquisto acquisto = new Acquisto();
        acquisto.setUtente(utente);
        for(DettaglioCarrello prod: listaProdotti) {
            Prodotto prodotto = prodottoRepository.findById(prod.getProdotto().getId()); //prendo il prodotto con LOCK perchè mentre si acquista nessuno deve poterlo modificare
            prodotto.setQuantita(prodotto.getQuantita()-prod.getQuantita());

            ProdottoAcquisto ps = new ProdottoAcquisto();
            ps.setProdotto(prodotto);
            ps.setPrezzo(prod.getPrezzo());
            ps.setQuantita(prod.getQuantita());
            ps.setAcquisto(acquisto);

            prodottoRepository.save(prodotto);
            acquistoRepository.save(acquisto);
            prodottoAcquistoRepository.save(ps);
        }
        Carrello carrelloNew = new Carrello();
        carrelloRepository.save(carrelloNew);
        utente.setCarrello(carrelloNew);
        carrelloRepository.delete(carrello);
    }
}
