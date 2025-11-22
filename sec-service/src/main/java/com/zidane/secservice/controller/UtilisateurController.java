package com.zidane.secservice.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zidane.secservice.dto.AuthentificationDTO;
import com.zidane.secservice.dto.InscriptionDTO;
import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.securite.JwtService;
import com.zidane.secservice.service.AccountService;
import com.zidane.secservice.service.ValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UtilisateurController {

    private final AccountService accountService;
    private final ValidationService validationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;    
    @PostMapping("/inscription")
    public ResponseEntity<AppUser> inscription(@RequestBody InscriptionDTO appUser) {

        if (appUser == null) {
            log.warn("L'utilisateur ne peut pas etre null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (appUser.getRoleName() == null || appUser.getRoleName().equals("")) {
            log.warn("Le role de l'utilisateur est obligatoire");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        AppRole appRole = accountService.loadRoleByRoleName(appUser.getRoleName());
        if (appRole == null) {
            log.warn("Le role " + appUser.getRoleName() + " n'existe pas");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (appUser.getAppUser().getEmail() == null || appUser.getAppUser().getEmail().equals("")) {

            log.warn("L'email de l'utilisateur est obligatoire");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!appUser.getAppUser().getEmail().contains("@") || !appUser.getAppUser().getEmail().contains(".")) {
            log.warn("L'email de l'utilisateur est invalide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AppUser> userOptional = accountService.loadUserByUsermail(appUser.getAppUser().getEmail()); // remplacer par la recherche effective de l'utilisateur

        if (userOptional.isPresent()) {
            log.warn("Un utilisateur avec cet email existe deja");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        appUser.getAppUser().getAppRoles().add(appRole);

        AppUser appUser2 = accountService.AddNewUser(appUser.getAppUser());

        validationService.saveValidation(appUser2);

        return ResponseEntity.status(HttpStatus.CREATED).body(appUser2);

    }
    
    @PostMapping("/activation")
    public ResponseEntity<AppUser> activation(@RequestBody Map<String, String> code) {

        if (code == null || code.get("code") == null || code.get("code").equals("")) {
            log.warn("Le code de validation est obligatoire");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        AppUser appUser = accountService.Activation(code);

        if (appUser == null) {
            log.warn("Le code de validation est invalide ou a expiré");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(appUser);

    }
    

    @PostMapping("/desactivation")
    public ResponseEntity<AppUser> desactivation(@RequestBody Map<String, String> email) {

        if (email == null || email.get("email") == null || email.get("email").equals("")) {
            log.warn("L'email est obligatoire");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        AppUser appUser = accountService.desactivationAppuser(email.get("email"));

        if (appUser == null) {
            log.warn("L'email est invalide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(appUser);

    }
    
    @PostMapping("/connexion")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO credentials) {
       
      System.out.println("Authentification au système");

      final Authentication authentication= this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
            
        
      );
        

      if (authentication.isAuthenticated())
      {
        

      return jwtService.generate(credentials.username());
         
      }

      System.out.println("User " + credentials.username() + " authenticated successfully");
       

      return null;

       
    }
    
}
