/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.schedule.dto;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "COW_ScheduleAppointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAppointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_1", referencedColumnName = "idWallet")
    @JsonBackReference
    private User veterinary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_2", referencedColumnName = "idWallet")
    @JsonBackReference
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @Column(nullable = false)
    private Date scheduleDate;

    @Column(nullable = false, length = 50)
    @Nationalized
    private String motive;

    @Column(nullable = false)
    private int scheduleStatus;

    @OneToOne
    @JoinColumn(name = "id_appointment")
    private Appointment appointment;
}
