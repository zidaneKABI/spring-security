package com.zidane.secservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zidane.secservice.repository.AppRoleRepository;
import com.zidane.secservice.repository.AppUserRepository;
import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImplementation implements AccountService 
{

    private     final AppUserRepository appUserRepository;
    private  final  AppRoleRepository appRoleRepository;
    private  final  PasswordEncoder passwordEncoder;
    

   

    @Override
    public AppUser AddNewUser(AppUser appUser) {
        String encodedPassword=appUser.getPassword();
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



}
