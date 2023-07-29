package com.example.progettoflesca.services;

import com.example.progettoflesca.authentication.Utils;
import com.example.progettoflesca.entities.Acquisto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettoflesca.repositories.AcquistoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultareAcquistiService {
    @Autowired
    private AcquistoRepository acquistoRepository;

    @Transactional(readOnly = true)
    public List<Acquisto> consultareAcquisti(int numPagine, int dimPagina, String order) {
        Pageable paging = PageRequest.of(numPagine, dimPagina, Sort.by(order));
        Page<Acquisto> pagedResult = acquistoRepository.findByEmail(Utils.getEmail(), paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    /*@Transactional(readOnly = true)
    public List<Acquisto> getAcquistiByUserInPeriod(Date startDate, Date endDate) throws RangeDataErratoException {
        /*if ( !utenteRepository.existsByEmail(email) ) {
            throw new UtenteNonEsisteException(email);
        }*
        if ( startDate.compareTo(endDate) <= 0 ) {
            throw new RangeDataErratoException();
        }
        return acquistoRepository.findByAcquirenteInData(startDate, endDate, Utils.getEmail());
    }*/
}