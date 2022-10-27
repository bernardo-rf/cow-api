package cow.starter.AppointmentRequest.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {
    @Query("SELECT a FROM AppointmentRequest a WHERE a.idUser = :idUser ORDER BY a.appointmentRequestStatus ASC, a.appointmentDate ASC")
    List<AppointmentRequest> getAllUserAppointmentRequest(long idUser);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.idBovine = :idBovine ORDER BY a.appointmentRequestStatus ASC, a.appointmentDate ASC")
    List<AppointmentRequest> getAllBovineAppointmentRequest(long idBovine);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.idUserRequest = :idUserRequest ORDER BY a.appointmentRequestStatus ASC, a.appointmentDate ASC")
    List<AppointmentRequest> getAllUserRequestAppointmentRequest(long idUserRequest);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.idAppointmentRequest = :idAppointmentRequest")
    AppointmentRequest getAppointmentRequest(long idAppointmentRequest);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.idBovine = :idBovine and a.appointmentDate = :appointmentDate")
    AppointmentRequest checkAppointmentRequest(long idBovine, Date appointmentDate);

}
