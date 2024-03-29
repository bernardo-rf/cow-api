/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateDTO {
    private long idAppointmentRequest;
    private long idVeterinary;
    private long idBovine;
    private Date appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private int status;
}
