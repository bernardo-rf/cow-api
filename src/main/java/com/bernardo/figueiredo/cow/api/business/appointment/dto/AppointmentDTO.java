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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private long idAppointment;
    private String idContract;
    private long idAppointmentRequest;
    private long idBovine;
    private long idUser;
    private String appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private int status;

    public AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getIdAppointment(),
                appointment.getIdContract(),
                appointment.getAppointmentRequest().getIdAppointmentRequest(),
                appointment.getBovine().getIdBovine(),
                appointment.getUser().getIdUser(),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentType(),
                appointment.getCost(),
                appointment.getObservation(),
                appointment.getAppointmentStatus());
    }
}
