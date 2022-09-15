package hedera.starter.cowAppointment.models;

import hedera.starter.cowBovine.models.Bovine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
