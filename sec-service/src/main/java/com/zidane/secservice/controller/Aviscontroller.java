package com.zidane.secservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.zidane.secservice.dto.AvisDTO;
import com.zidane.secservice.sec.entities.Avis;
import com.zidane.secservice.service.AvisService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/avis")
@AllArgsConstructor
public class Aviscontroller {

    private final AvisService avisService;

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)    
    public void creerAvis(@RequestBody AvisDTO avisDTO) {
        avisService.creerAvis(avisDTO);
    }

    @GetMapping
    public List<AvisDTO> listerAvis() {
        List<Avis> avisList = avisService.listerAvis();
        List<AvisDTO> avisDTOList = new java.util.ArrayList<>();
        for (Avis avis : avisList) {
            avisDTOList.add(new AvisDTO(avis.getMessage(), avis.getStatut()));
            // Convert each Avis to AvisDTO and add to a list
        }
        return avisDTOList;
    }
    
}
