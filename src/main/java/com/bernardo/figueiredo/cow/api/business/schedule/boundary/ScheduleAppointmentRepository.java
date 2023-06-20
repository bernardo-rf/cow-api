/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.schedule.boundary;

import com.bernardo.figueiredo.cow.api.business.schedule.dto.ScheduleAppointment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleAppointmentRepository extends JpaRepository<ScheduleAppointment, Long> {

    @Query("SELECT a FROM ScheduleAppointment a WHERE a.id = :id")
    ScheduleAppointment getScheduleAppointmentById(long id);

    @Query(
            "SELECT a FROM ScheduleAppointment a WHERE a.bovine.idBovine = :id ORDER BY a.scheduleStatus ASC, a.scheduleDate ASC")
    List<ScheduleAppointment> getScheduleAppointmentByBovineId(long id);

    @Query(
            "SELECT a FROM ScheduleAppointment a WHERE a.veterinary.idUser = :id ORDER BY a.scheduleStatus ASC, a.scheduleDate ASC")
    List<ScheduleAppointment> getScheduleAppointmentByVeterinaryId(long id);

    @Query(
            "SELECT a FROM ScheduleAppointment a WHERE a.owner.idUser = :id ORDER BY a.scheduleStatus ASC, a.scheduleDate ASC")
    List<ScheduleAppointment> getScheduleAppointmentByOwnerId(long id);
}
