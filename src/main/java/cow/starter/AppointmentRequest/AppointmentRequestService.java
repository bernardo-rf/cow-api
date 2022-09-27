package cow.starter.AppointmentRequest;

import cow.starter.AppointmentRequest.models.AppointmentRequest;
import cow.starter.AppointmentRequest.models.AppointmentRequestCreateDTO;
import cow.starter.AppointmentRequest.models.AppointmentRequestDTO;
import cow.starter.AppointmentRequest.models.AppointmentRequestRepository;
import cow.starter.Bovine.models.Bovine;
import cow.starter.Bovine.models.BovineRepository;
import cow.starter.User.models.User;
import cow.starter.User.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentRequestService {

    @Autowired
    AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineRepository bovineRepository;

    public AppointmentRequestDTO convertToDTO(AppointmentRequest appointmentRequest) {
        return new AppointmentRequestDTO(appointmentRequest.getIdAppointmentRequest(), appointmentRequest.getIdUser(),
                appointmentRequest.getIdUserRequest(), appointmentRequest.getIdBovine(),
                appointmentRequest.getAppointmentDate(), appointmentRequest.getMotive(),
                appointmentRequest.getStatus());
    }

    public List<AppointmentRequestDTO> getAppointmentsRequest(List<AppointmentRequest> appointmentRequestList){
        List<AppointmentRequestDTO> appointmentRequestDTOList = new ArrayList<>();
        if (!appointmentRequestList.isEmpty()) {
            for (AppointmentRequest appointmentRequest : appointmentRequestList) {
                appointmentRequestDTOList.add(convertToDTO(appointmentRequest));
            }
            return appointmentRequestDTOList;
        }
        return appointmentRequestDTOList;
    }

    public boolean checkAppointmentRequestValues(long idUser, long idUserRequest, long idBovine){
        User user = userRepository.getUser(idUser);
        if (user != null) {
            User userRequest = userRepository.getUser(idUserRequest);
            if (userRequest != null) {
                Bovine bovine = bovineRepository.getBovine(idBovine);
                if (bovine != null){
                    return bovine.getIdBovine() != 0;
                }
            }
        }
        return false;
    }

    public AppointmentRequestDTO createAppointmentRequest(AppointmentRequestCreateDTO appointmentRequestCreateDTO) {
        AppointmentRequestDTO appointmentRequestDTO = new AppointmentRequestDTO();
        try {
            AppointmentRequest appointmentRequest = appointmentRequestRepository.checkAppointmentRequest(
                    appointmentRequestCreateDTO.getIdBovine(),
                    appointmentRequestCreateDTO.getAppointmentDate());
            if (appointmentRequest == null && checkAppointmentRequestValues( appointmentRequestCreateDTO.getIdUser(),
                    appointmentRequestCreateDTO.getIdUserRequest(), appointmentRequestCreateDTO.getIdBovine())) {
                AppointmentRequest newAppointmentRequest = new AppointmentRequest(appointmentRequestCreateDTO.getIdUser(),
                        appointmentRequestCreateDTO.getIdUserRequest(), appointmentRequestCreateDTO.getIdBovine(),
                        appointmentRequestCreateDTO.getAppointmentDate(), appointmentRequestCreateDTO.getMotive(),
                        appointmentRequestCreateDTO.getStatus());
                appointmentRequestRepository.save(newAppointmentRequest);

                if(newAppointmentRequest.getIdAppointmentRequest() != 0) {
                    appointmentRequestDTO = convertToDTO(newAppointmentRequest);
                    return appointmentRequestDTO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentRequestDTO;
    }

    public AppointmentRequestDTO updateAppointmentRequest(AppointmentRequestDTO appointmentRequestDTO) {
        AppointmentRequestDTO emptyAppointmentRequestDTO = new AppointmentRequestDTO();
        try {
            if (checkAppointmentRequestValues(appointmentRequestDTO.getIdUser(),
                    appointmentRequestDTO.getIdUserRequest(),
                    appointmentRequestDTO.getIdBovine())){
                AppointmentRequest appointmentRequest = appointmentRequestRepository.getAppointmentRequest(appointmentRequestDTO.getIdAppointmentRequest());
                if (appointmentRequest != null) {
                    appointmentRequest.setIdBovine(appointmentRequestDTO.getIdBovine());
                    appointmentRequest.setIdUser(appointmentRequestDTO.getIdUser());
                    appointmentRequest.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());
                    appointmentRequest.setMotive(appointmentRequestDTO.getMotive());
                    appointmentRequest.setStatus(appointmentRequestDTO.getStatus());
                    appointmentRequestRepository.save(appointmentRequest);
                    return convertToDTO(appointmentRequest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emptyAppointmentRequestDTO;
    }

    public boolean deleteAppointmentRequest(long idAppointmentRequest){
        try {
            AppointmentRequest appointmentToRequest = appointmentRequestRepository.getAppointmentRequest(idAppointmentRequest);
            if (appointmentToRequest != null) {
                appointmentRequestRepository.delete(appointmentToRequest);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
