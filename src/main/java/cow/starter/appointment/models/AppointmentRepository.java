package cow.starter.appointment.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.user.idUser = :idUser ORDER BY a.appointmentDate ASC")
    List<Appointment> getAllUserAppointment(long idUser);

    @Query("SELECT a FROM Appointment a WHERE a.bovine.idBovine = :idBovine ORDER BY a.appointmentDate ASC")
    List<Appointment> getAllBovineAppointment(long idBovine);

    @Query("SELECT a FROM Appointment a WHERE a.bovine.idBovine in (SELECT b.idBovine FROM Bovine b WHERE b.user.idWallet = :idOwner) ORDER BY a.appointmentDate ASC")
    List<Appointment> getAllOwnedBovineAppointment(String idOwner);

    @Query("SELECT a FROM Appointment a WHERE a.idAppointment = :idAppointment")
    Appointment getAppointment(long idAppointment);
}
