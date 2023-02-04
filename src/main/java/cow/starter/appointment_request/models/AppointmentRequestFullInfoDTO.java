/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.appointment_request.models;

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
