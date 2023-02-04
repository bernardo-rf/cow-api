/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.appointment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFullInfoDTO {
    private long idAppointment;
    private String idContract;
    private long idAppointmentRequest;
    private long idBovine;
    private long idUser;
    private String appointmentDate;
    private String appointmentType;
    private Double cost;
    private String observation;
    private long bovineSerialNumber;
    private int status;
}
