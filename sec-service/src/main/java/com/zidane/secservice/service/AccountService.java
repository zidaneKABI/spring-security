package com.zidane.secservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;

public interface AccountService {

    AppUser AddNewUser(AppUser appUser);
    AppRole AddNewRole(AppRole appRole);

    void AddRoleToUser(String username, String roleName);
     
    AppRole loadRoleByRoleName(String roleName);
    Optional<AppUser> loadUserByUsermail(String mail);
    AppUser myloadUserByUsername(String username);

    List<AppUser> listUsers();
    
    AppUser Activation(Map<String, String> code);

    AppUser desactivationAppuser(String email);
    
}
