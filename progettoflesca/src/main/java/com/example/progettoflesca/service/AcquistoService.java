package com.example.progettoflesca.service;

import com.example.progettoflesca.authentication.Utils;
import com.example.progettoflesca.entities.*;
import com.example.progettoflesca.exception.PrezzoCambiatoException;
import com.example.progettoflesca.exception.QuantitaInsufficienteException;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettoflesca.repositories.AcquistoRepository;
import com.example.progettoflesca.repositories.ProdottoAcquistoRepository;
import com.example.progettoflesca.repositories.ProdottoRepository;
import com.example.progettoflesca.repositories.UtenteRepository;

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

    @Transactional(readOnly = false, rollbackFor = {QuantitaInsufficienteException.class, PrezzoCambiatoException.class})
    public void acquista(List<DettaglioOrdineClient> carrelloClient) throws QuantitaInsufficienteException, PrezzoCambiatoException {

        //verificare se bastano i soldi? dovrei fare una sorta di pagamento?

        String email= Utils.getEmail();

        Utente utente = utenteRepository.findByEmail(email);
        Carrello carrello = utente.getCarrello();

        List<DettaglioCarrello> listaProdotti = carrello.getProdottiCarrello();

        for(DettaglioCarrello prod: listaProdotti) {
            //Prodotto prodotto = prodottoRepository.findByIdWithLock(prod.getProdotto().getId(), LockModeType.PESSIMISTIC_WRITE); //prendo il prodotto con LOCK perchè mentre si acquista nessuno deve poterlo modificare
            Prodotto prodotto = prodottoRepository.findById(prod.getProdotto().getId()); //prendo il prodotto con LOCK perchè mentre si acquista nessuno deve poterlo modificare

            for(DettaglioOrdineClient prodClient: carrelloClient) {
                if(prodClient.getIdProd() == prodotto.getId()) {//verifico del prezzo se è cambiato e se si ha il numero di pezzi
                    if (prodClient.getPrezzo() != prodotto.getPrezzo())
                        throw new PrezzoCambiatoException(prodotto.getId(), prodotto.getPrezzo());
                    if (prodotto.getQuantita() < prodClient.getQuantita())
                        throw new QuantitaInsufficienteException(prodotto.getId());
                }
            }
        }
        Acquisto acquisto = new Acquisto();
        acquisto.setUtente(utente);
        for(DettaglioOrdineClient p: carrelloClient) {
            //Prodotto prodotto = prodottoRepository.findByIdWithLock(p.getIdProd(), LockModeType.PESSIMISTIC_WRITE); //prendo il prodotto con LOCK perchè mentre si acquista nessuno deve poterlo modificare
            Prodotto prodotto = prodottoRepository.findById(p.getIdProd()); //prendo il prodotto con LOCK perchè mentre si acquista nessuno deve poterlo modificare
            prodotto.setQuantita(prodotto.getQuantita()-p.getQuantita());

            ProdottoAcquisto ps = new ProdottoAcquisto();
            ps.setProdotto(prodotto);
            ps.setPrezzo(p.getPrezzo());
            ps.setQuantita(p.getQuantita());
            ps.setAcquisto(acquisto);

            prodottoRepository.save(prodotto);
            acquistoRepository.save(acquisto);
            prodottoAcquistoRepository.save(ps);

        }
    }
}
