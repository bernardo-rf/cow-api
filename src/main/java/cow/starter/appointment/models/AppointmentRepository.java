package cow.starter.Appointment.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.idUser = :idUser ORDER BY a.appointmentDate ASC")
    List<Appointment> getAllUserAppointment(long idUser);

    @Query("SELECT a FROM Appointment a WHERE a.idBovine = :idBovine ORDER BY a.appointmentDate ASC")
    List<Appointment> getAllBovineAppointment(long idBovine);

    @Query("SELECT a FROM Appointment a WHERE a.idBovine in (SELECT b.idBovine FROM Bovine b WHERE b.idOwner = :idOwner) ORDER BY a.appointmentDate ASC")
    List<Appointment> getAllOwnedBovineAppointment(String idOwner);

    @Query("SELECT a FROM Appointment a WHERE a.idAppointment = :idAppointment")
    Appointment getAppointment(long idAppointment);

    @Query("SELECT a FROM Appointment a WHERE a.idBovine = :idBovine and a.appointmentDate = :appointmentDate")
    Appointment checkAppointment(long idBovine, Date appointmentDate);
}
