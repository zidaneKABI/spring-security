package com.zidane.secservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.sec.entities.Validation;


public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Validation findByCode(String code);
    Validation findByAppUser(AppUser appUser);

}
