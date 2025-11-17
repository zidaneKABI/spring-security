package com.zidane.secservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zidane.secservice.sec.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    
    AppRole findByRoleName(String roleName);

}
