/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long idUser;
    private String idContract;
    private String idWallet;
    private int idUserType;
    private String name;
    private Date birthDate;
    private String email;
    private String password;
    private Boolean active;
    private Double balance;
    private String fullName;
    private String imageCID;
}
