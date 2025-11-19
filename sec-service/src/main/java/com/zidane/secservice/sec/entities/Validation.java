package com.zidane.secservice.sec.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Validation {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;
    
    private Instant dateCreation;
    private Instant dateExpiration;
    private Instant dateValidation;
    private boolean valide;
    private String  code;


    @OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
    private AppUser appUser;

   

}
