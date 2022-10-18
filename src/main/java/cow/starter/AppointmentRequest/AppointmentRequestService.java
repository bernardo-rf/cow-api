package cow.starter.AppointmentRequest;

import cow.starter.AppointmentRequest.models.*;
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

    public AppointmentRequestFullInfoDTO convertFullInfoToDTO(AppointmentRequest appointmentRequest, String userName,
                                              String userRequestName, long serialNumber) {
        return new AppointmentRequestFullInfoDTO(appointmentRequest.getIdAppointmentRequest(),
                appointmentRequest.getIdUser(), appointmentRequest.getIdUserRequest(), appointmentRequest.getIdBovine(),
                appointmentRequest.getAppointmentDate(), appointmentRequest.getMotive(),
                appointmentRequest.getStatus(), userName, userRequestName, serialNumber);
    }

    public List<AppointmentRequestFullInfoDTO> getAppointmentsRequest(List<AppointmentRequest> appointmentRequestList){
        List<AppointmentRequestFullInfoDTO> appointmentRequestDTOList = new ArrayList<>();
        if (!appointmentRequestList.isEmpty()) {
            for (AppointmentRequest appointmentRequest : appointmentRequestList) {
                User userVeterinary = userRepository.getUser(appointmentRequest.getIdUser());
                User user = userRepository.getUser(appointmentRequest.getIdUserRequest());
                Bovine bovine = bovineRepository.getBovine(appointmentRequest.getIdBovine());
                appointmentRequestDTOList.add(convertFullInfoToDTO(appointmentRequest, userVeterinary.getName(),
                        user.getName(), bovine.getSerialNumber()));
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

    public List<AppointmentRequestDTO> createAppointmentRequest(AppointmentRequestCreateDTO appointmentRequestCreateDTO) {
        List<AppointmentRequestDTO> appointmentRequestDTOList = new ArrayList<>();
        try {
            for (Bovine bovine: appointmentRequestCreateDTO.getBovines()) {
                AppointmentRequest appointmentRequest = appointmentRequestRepository.checkAppointmentRequest(
                        bovine.getIdBovine(), appointmentRequestCreateDTO.getAppointmentDate());
                if (appointmentRequest == null && checkAppointmentRequestValues(appointmentRequestCreateDTO.getIdUser(),
                        appointmentRequestCreateDTO.getIdUserRequest(), bovine.getIdBovine())) {
                    AppointmentRequest newAppointmentRequest = new AppointmentRequest(
                            appointmentRequestCreateDTO.getIdUser(),
                            appointmentRequestCreateDTO.getIdUserRequest(),
                            bovine.getIdBovine(),
                            appointmentRequestCreateDTO.getAppointmentDate(),
                            appointmentRequestCreateDTO.getMotive(),
                            appointmentRequestCreateDTO.getStatus());
                    appointmentRequestRepository.save(newAppointmentRequest);
                    if(newAppointmentRequest.getIdAppointmentRequest() != 0) {
                        appointmentRequestDTOList.add(convertToDTO(newAppointmentRequest));
                    }
                }
            }
            return appointmentRequestDTOList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentRequestDTOList;
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
