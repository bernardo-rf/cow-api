/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.idAppointment = :id ORDER BY a.appointmentDate ASC")
    Appointment getAppointmentById(long id);

    @Query("SELECT a FROM Appointment a WHERE a.bovine.idBovine = :id ORDER BY a.appointmentDate ASC")
    List<Appointment> getAppointmentsByBovineId(long id);

    @Query("SELECT a FROM Appointment a WHERE a.user.idUser = :id ORDER BY a.appointmentDate ASC")
    List<Appointment> getAppointmentsByUserId(long id);

    @Query("SELECT a FROM Appointment a WHERE a.user.idWallet = :address ORDER BY a.appointmentDate ASC")
    List<Appointment> getAppointmentsByUserAddress(String address);
}
