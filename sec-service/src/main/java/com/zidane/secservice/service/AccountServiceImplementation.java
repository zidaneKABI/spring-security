package com.zidane.secservice.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zidane.secservice.repository.AppRoleRepository;
import com.zidane.secservice.repository.AppUserRepository;
import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.sec.entities.Validation;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImplementation implements AccountService 
{

    private      final AppUserRepository appUserRepository;
    private      final  AppRoleRepository appRoleRepository;
    private      final  PasswordEncoder passwordEncoder;
    private      final ValidationService validationService;

   

    @Override
    public AppUser AddNewUser(AppUser appUser) {
        String encodedPassword=appUser.getPwd();
        appUser.setPwd(passwordEncoder.encode(encodedPassword));
       return appUserRepository.save(appUser);
    }

    @Override
    public AppRole AddNewRole(AppRole appRole) {
                return appRoleRepository.save(appRole);
      }

    @Override
    public void AddRoleToUser(String username, String roleName) {
      
        AppUser appUser=appUserRepository.findByName(username);
        AppRole appRole=appRoleRepository.findByRoleName(roleName);
        appUser.getAppRoles().add(appRole);
   
    }

    @Override
    public AppUser loadUserByUsername(String username) 
    {
        return appUserRepository.findByName(username);
        
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }


    @Override
    public Optional<AppUser> loadUserByUsermail(String mail) {
        return appUserRepository.findByEmail(mail);
    }

    @Override
    public AppRole loadRoleByRoleName(String roleName) {
        return appRoleRepository.findByRoleName(roleName);
    }

    @Override
    public AppUser Activation(Map<String, String> code) {

        System.out.println("code received in service: " + code.get("code"));

        Validation validation = validationService.getValidationByCode(code.get("code"));

        if (validation != null) {
            AppUser appUser = validation.getAppUser();
            appUser.setActif(true);
            appUserRepository.save(appUser);

            validation.setValide(true);
            validation.setDateValidation(Instant.now());
            validationService.updateValidation(validation);

            return appUser;
        } else {
            return null;
        }

    }

    @Override
    public AppUser desactivationAppuser(String email) {
 
 
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            AppUser appUser = userOptional.get();
            appUser.setActif(false);


            appUserRepository.save(appUser);
            
            validationService.deleteValidation(appUser);
            
            return appUser;
        } else {
            return null;
        }
    }
    
    

}
