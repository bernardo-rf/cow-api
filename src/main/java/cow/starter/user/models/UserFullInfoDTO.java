/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFullInfoDTO {
    private long idUser;
    private String idContract;
    private String idWallet;
    private long idUserType;
    private String type;
    private String name;
    private Date birthDate;
    private String email;
    private String password;
    private Boolean active;
    private Double balance;
    private String fullName;
    private String imageCID;
}