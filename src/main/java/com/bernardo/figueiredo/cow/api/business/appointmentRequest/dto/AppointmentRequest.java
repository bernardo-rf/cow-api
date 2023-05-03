/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.appointmentRequest.dto;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="COW_AppointmentRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAppointmentRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_1", referencedColumnName = "idWallet")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_2", referencedColumnName = "idWallet")
    @JsonBackReference
    private User userRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bovine")
    @JsonBackReference
    private Bovine bovine;

    @OneToOne
    @JoinColumn(name="id_appointment")
    private Appointment appointment;

    @Column(nullable = false)
    private Date appointmentRequestDate;

    @Column(nullable = false, length = 50)
    @Nationalized
    private String motive;

    @Column(nullable = false)
    private int appointmentRequestStatus;

    public AppointmentRequest(User user, User userRequest, Bovine bovine, Date appointmentRequestDate, String motive,
                              int status ) {
        this.user = user;
        this.userRequest = userRequest;
        this.bovine = bovine;
        this.appointmentRequestDate = appointmentRequestDate;
        this.motive = motive;
        this.appointmentRequestStatus = status;
    }
}
