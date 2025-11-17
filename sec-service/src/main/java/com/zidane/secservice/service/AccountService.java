package com.zidane.secservice.service;

import java.util.List;

import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;

public interface AccountService {

    AppUser AddNewUser(AppUser appUser);
    AppRole AddNewRole(AppRole appRole);

    void    AddRoleToUser(String username, String roleName);

    AppUser loadUserByUsername(String username);
    List    <AppUser> listUsers();
}
