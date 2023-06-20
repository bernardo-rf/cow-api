/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.dto;

import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.schedule.dto.ScheduleAppointment;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "COW_Appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Nationalized
    private String idContract;

    @OneToOne
    @JoinColumn(name = "id_schedule_appointment")
    private ScheduleAppointment scheduleAppointment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_1", referencedColumnName = "idWallet")
    @JsonBackReference
    private User veterinary;

    @Column(nullable = false)
    private Date appointmentDate;

    @Column(nullable = false, length = 50)
    @Nationalized
    private String appointmentType;

    @Column(nullable = false, scale = 2)
    private Double cost;

    @Nationalized
    private String observation;

    @Column(nullable = false)
    private int appointmentStatus;
}
