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

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestCreateDTO {
    private long idUser;
    private long idUserRequest;
    private Date appointmentDate;
    private String motive;
    private int status;
    private ArrayList<Integer> bovineIds;
}
