package com.zidane.secservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zidane.secservice.sec.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    
    AppUser findByName(String name);
    Optional<AppUser> findByEmail(String email);

}
