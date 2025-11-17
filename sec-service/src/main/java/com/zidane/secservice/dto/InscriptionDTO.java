package com.zidane.secservice.dto;

import com.zidane.secservice.sec.entities.AppUser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InscriptionDTO {

    private AppUser appUser;
    private String  roleName;
}
