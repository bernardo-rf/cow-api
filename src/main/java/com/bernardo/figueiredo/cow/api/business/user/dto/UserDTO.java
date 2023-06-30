/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.user.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String idContract;
    private String idWallet;
    private long idUserType;
    private String name;
    private Instant birthDate;
    private String email;
    private String password;
    private Boolean active;
    private Double balance;
    private String fullName;
    private String imageCID;
}
