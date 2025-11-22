package com.zidane.secservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zidane.secservice.sec.entities.AppRole;
import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private  final AccountService accountService;
    
    @GetMapping("/users")
    public List<AppUser> listUsers() {
        return accountService.listUsers();
    }
    
    @GetMapping("/users/{username}")
    public AppUser getUserById(@PathVariable String username) {
        return accountService.myloadUserByUsername(username);
    }

    @PostMapping(path="/roles")
    public AppRole addNewRole(@RequestBody AppRole appRole) {
        return accountService.AddNewRole(appRole);
    }

 
    
    @PostMapping(path="/users")
    public AppUser addNewUser(@RequestBody AppUser appUser) {
        return accountService.AddNewUser(appUser);
    }

    @PostMapping(path="/addroletouser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.AddRoleToUser(roleUserForm.getUsername(), roleUserForm.getRolename());
    }

    

}
