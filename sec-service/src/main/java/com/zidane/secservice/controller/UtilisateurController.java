package com.zidane.secservice.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zidane.secservice.dto.InscriptionDTO;
import com.zidane.secservice.repository.AppRoleRepository;
import com.zidane.secservice.repository.AppUserRepository;
import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UtilisateurController {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    @PostMapping("/inscription")
    public ResponseEntity<AppUser> inscription(@RequestBody InscriptionDTO appUser) {

        if (appUser == null)
            throw new RuntimeException("L'utilisateur ne peut pas etre null");
        
        if (appUser.getRoleName() == null || appUser.getRoleName().equals("")) {
            throw new RuntimeException("Le role de l'utilisateur est obligatoire");
        }
        

        AppRole appRole = appRoleRepository.findByRoleName( appUser.getRoleName());
        if (appRole == null) {
            throw new RuntimeException("Le role " + appUser.getRoleName() + " n'existe pas");
        }
        
        
        
        
        if (appUser.getAppUser().getEmail() == null || appUser.getAppUser().getEmail().equals("")) {

            throw new RuntimeException("L'email de l'utilisateur est obligatoire");
        }
        
        if(!appUser.getAppUser().getEmail().contains("@")  || !appUser.getAppUser().getEmail().contains(".")) {
            throw new RuntimeException("L'email de l'utilisateur est invalide");
        }
        
        Optional<AppUser> userOptional = appUserRepository.findByEmail(appUser.getAppUser().getEmail());  // remplacer par la recherche effective de l'utilisateur
        
        if (userOptional.isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe deja");
        }
        
        appUser.getAppUser().getAppRoles().add(appRole);

        
       return ResponseEntity.status(HttpStatus.CREATED).body(appUserRepository.save(appUser.getAppUser()));

    }
    
    

}
