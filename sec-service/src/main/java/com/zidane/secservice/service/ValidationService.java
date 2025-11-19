package com.zidane.secservice.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.zidane.secservice.repository.ValidationRepository;
import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.sec.entities.Validation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidationService {

  private final ValidationRepository validationRepository;
  private final NotificationService notificationService;
  
  public void saveValidation(AppUser appUser) {

    Validation validation = new Validation();
    validation.setAppUser(appUser);
    Instant now = Instant.now();
    validation.setDateCreation(now);
    validation.setDateExpiration(validation.getDateCreation().plus(10, ChronoUnit.MINUTES)); // 24 hours validity
    validation.setValide(false);

    validation.setCode(java.util.UUID.randomUUID().toString());

    int codeNumber = new Random().nextInt(999999); // 6-digit code
    String codeString = String.format("%06d", codeNumber);

    validation.setCode(codeString);

    validationRepository.save(validation);
    notificationService.sendValidationEmail(validation);

  }
   
  public Validation getValidationByCode(String code)
  {

    System.out.println("code received: " + code);

    Validation validation = validationRepository.findByCode(code);

    if (validation != null && !validation.isValide() && validation.getDateExpiration().isAfter(Instant.now())) {
      validationRepository.save(validation);
      return validation;
    } else {
      return null;
    }
  }
  
  public void updateValidation(Validation validation) {
    validationRepository.save(validation);
  }
  
  public void deleteValidation(AppUser appUser) {
    
    Validation validation = validationRepository.findByAppUser(appUser);

    validationRepository.delete(validation);
   
    System.out.println("Validation record deleted for user: " + appUser.getName()); 
  }

}
