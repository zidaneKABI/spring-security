package com.zidane.secservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zidane.secservice.dto.AvisDTO;
import com.zidane.secservice.repository.AvisRepository;
import com.zidane.secservice.sec.entities.Avis;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AvisService {

    private final AvisRepository avisRepository;
    

    public Avis creerAvis(AvisDTO avisdto) {
        
        if (avisRepository.findByMessage(avisdto.getMessage()) != null) {
            throw new RuntimeException("Avis with message " + avisdto.getMessage() + " already exists");
        }
        
        Avis avis = new Avis();
        avis.setMessage(avisdto.getMessage());
        avis.setStatut(avisdto.getStatut());

        return avisRepository.save(avis);
    }


    public List<Avis> listerAvis() {
        return avisRepository.findAll();
    }   

}
