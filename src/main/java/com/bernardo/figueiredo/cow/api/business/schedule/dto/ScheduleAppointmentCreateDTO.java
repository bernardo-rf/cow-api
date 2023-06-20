/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.schedule.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAppointmentCreateDTO {
    private long idVeterinary;
    private long idOwner;
    private long idBovine;
    private String motive;
    private Date scheduleDate;
    private int scheduleStatus;
}
