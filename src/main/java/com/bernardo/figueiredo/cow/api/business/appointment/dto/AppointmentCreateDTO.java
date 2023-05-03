/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.dto;

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
public class AppointmentCreateDTO {
    private long idAppointmentRequest;
    private long idUser;
    private Date appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private int status;
    private ArrayList<Integer> bovineIds;
}
