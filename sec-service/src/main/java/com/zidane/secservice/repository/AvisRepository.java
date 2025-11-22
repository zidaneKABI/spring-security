package com.zidane.secservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zidane.secservice.sec.entities.Avis;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    
    Avis findByMessage(String title);

}
