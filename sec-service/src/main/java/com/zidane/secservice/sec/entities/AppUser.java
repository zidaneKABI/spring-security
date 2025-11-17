package com.zidane.secservice.sec.entities;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Utilisateurs")
public class AppUser implements  UserDetails {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name ="password")
    private String pwd;
    
    private boolean actif = false;
    
    private boolean isAccountNonExpired=true;

    private String email;
    
    
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
    joinColumns = @jakarta.persistence.JoinColumn(name = "USER_ID", referencedColumnName = "id"),
    inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "ROLE_ID", referencedColumnName = "id")
        
    )
    
    private Collection<AppRole> appRoles = new java.util.ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return this.appRoles;
    }
    
    @Override
    public boolean isAccountNonExpired() {
           return isAccountNonExpired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return actif;
    }


    @Override
    public boolean isEnabled() {
        return actif;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public AppUser(Long id, String name, String pwd, Collection<AppRole> appRoles) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.appRoles = appRoles;
    }
    
      


}
