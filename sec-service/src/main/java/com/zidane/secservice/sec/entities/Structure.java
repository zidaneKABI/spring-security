package com.zidane.secservice.sec.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Structure {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    
    
    

}
