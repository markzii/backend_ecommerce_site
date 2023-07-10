package com.example.progettoflesca.service;

import com.example.progettoflesca.entities.Categoria;
import com.example.progettoflesca.entities.Prodotto;
import com.example.progettoflesca.exception.*;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettoflesca.repositories.CategoriaRepository;
import com.example.progettoflesca.repositories.ProdottoRepository;

import java.util.ArrayList;
import java.util.List;

//service da parte dell'amministratore, permette l'aggiunta/rimozione e dell'aggionramento riguardo prezzi e quantita
//service che permette la stampa dei prodotti da parte di un acquirente
@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodRepository;
    @Autowired
    private CategoriaRepository categRepository;

    @Transactional(readOnly = true)
    public List<Prodotto> getTuttiProdotti(int numPagine, int dimPagina, String ordine){
        Pageable paging = PageRequest.of(numPagine, dimPagina, Sort.by(ordine));
        Page<Prodotto> pagedResult = prodRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Prodotto> getProdottiDalNome(String nome, int numPagine, int dimPagina, String ordine) {
        Pageable paging = PageRequest.of(numPagine, dimPagina, Sort.by(ordine));
        Page<Prodotto> pagedResult = prodRepository.findByNome(nome, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Prodotto getProdottoDalCodiceBarre(String codiceBarre) {
        return prodRepository.findBycodiceBarre(codiceBarre);
    }

    @Transactional(readOnly = true)
    public List<Prodotto> getProdottiDallaCategoria(String categoria, int numPagine, int dimPagina, String ordine) {
        Pageable paging = PageRequest.of(numPagine, dimPagina, Sort.by(ordine));
        Page<Prodotto> pagedResult = prodRepository.findBycategoria(categoria, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }
    @Transactional(readOnly = true)
    public Prodotto getProdottiDaId(int id) {
        //return prodRepository.findByIdWithLock(id, LockModeType.OPTIMISTIC);
        return prodRepository.findById(id);

    }


    //Servizi effettuabili solo dall'amministratore
    @Transactional(readOnly = false, rollbackFor = {ProdottoEsistenteException.class, NoCodiceBarreException.class})
    public Prodotto aggiungiProdotto(String codiceBarre, String nome, float prezzo, int quantita, String descrizione, String categoria) throws ProdottoEsistenteException, NoCodiceBarreException {
        if(codiceBarre!=null) throw new NoCodiceBarreException();
        if(prodRepository.existsBycodiceBarre(codiceBarre)) throw new ProdottoEsistenteException();
        Categoria cate = categRepository.findByNome(categoria);
        Prodotto prodotto= new Prodotto();
        prodotto.setNome(nome);
        prodotto.setCodiceBarre(codiceBarre);
        prodotto.setPrezzo(prezzo);
        prodotto.setQuantita(quantita);
        prodotto.setDescrizione(descrizione);
        prodotto.setCategoria(cate);
        return prodRepository.save(prodotto);
    }
    @Transactional(readOnly = false, rollbackFor = AggiornamentoErroreException.class)
    public Prodotto aggiornaProdotto(int id, int quantita, float prezzo) throws AggiornamentoErroreException {
        if(quantita < 0 || prezzo < 0)
            throw new AggiornamentoErroreException();
        //Prodotto prodotto= prodRepository.findByIdWithLock(id, LockModeType.PESSIMISTIC_WRITE);
    Prodotto prodotto= prodRepository.findById(id);
        prodotto.setPrezzo(prezzo);
        prodotto.setQuantita(prodotto.getQuantita()+quantita);
        return prodRepository.save(prodotto);
    }

    @Transactional(readOnly = false, rollbackFor = EliminazioneErroreException.class)
    public void eliminaProdotto(int id) throws EliminazioneErroreException {
        if(id <= 0) throw new EliminazioneErroreException();
        //Prodotto prodotto= prodRepository.findByIdWithLock(id, LockModeType.PESSIMISTIC_WRITE);
        Prodotto prodotto= prodRepository.findById(id);

        prodRepository.delete(prodotto);
    }


    /*Il contesto di persistenza di Spring rileverà automaticamente le modifiche apportate all'entità gestita.
    Non è necessario chiamare esplicitamente il metodo save().
    Quando viene eseguita una transazione o al termine di una transazione, il contesto di persistenza
    di Spring effettuerà automaticamente l'aggiornamento delle entità modificate nel database.

    lock ottimistico e pessimistico
    */
}
