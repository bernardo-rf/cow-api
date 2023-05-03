/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointmentRequest.boundary;

import com.bernardo.figueiredo.cow.api.business.appointmentRequest.dto.AppointmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {
    @Query("SELECT a FROM AppointmentRequest a WHERE a.user.idUser = :idUser ORDER BY a.appointmentRequestStatus ASC, a.appointmentRequestDate ASC")
    List<AppointmentRequest> getAllUserAppointmentRequest(long idUser);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.bovine.idBovine = :idBovine ORDER BY a.appointmentRequestStatus ASC, a.appointmentRequestDate ASC")
    List<AppointmentRequest> getAllBovineAppointmentRequest(long idBovine);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.userRequest.idUser = :idUserRequest ORDER BY a.appointmentRequestStatus ASC, a.appointmentRequestDate ASC")
    List<AppointmentRequest> getAllUserRequestAppointmentRequest(long idUserRequest);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.idAppointmentRequest = :idAppointmentRequest")
    AppointmentRequest getAppointmentRequest(long idAppointmentRequest);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.bovine.idBovine = :idBovine and a.appointmentRequestDate = :appointmentDate")
    AppointmentRequest checkAppointmentRequest(long idBovine, Date appointmentDate);

}
