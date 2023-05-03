/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointmentRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestFullInfoDTO {
    private long idAppointmentRequest;
    private long idUser;
    private long idUserRequest;
    private long idBovine;
    private String appointmentDate;
    private String motive;
    private int status;
    private String userName;
    private String userRequestName;
    private long bovineSerialNumber;
}
