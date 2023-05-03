/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointment.dto;

import com.bernardo.figueiredo.cow.api.business.appointmentRequest.dto.AppointmentRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "COW_Appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAppointment;

    @Column(nullable = false)
    @Nationalized
    private String idContract;

    @OneToOne
    @JoinColumn(name="id_appointment_request")
    private AppointmentRequest appointmentRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

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

    public Appointment(String idContract, AppointmentRequest appointmentRequest, Bovine bovine, User user, Date appointmentDate,
                       String appointmentType, Double cost, String observation, int status) {
        this.idContract = idContract;
        this.appointmentRequest = appointmentRequest;
        this.bovine = bovine;
        this.user = user;
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.cost = cost;
        this.observation = observation;
        this.appointmentStatus = status;
    }
}
