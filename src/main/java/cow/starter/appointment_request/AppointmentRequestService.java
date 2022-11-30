package cow.starter.appointment_request;

import cow.starter.appointment.AppointmentService;
import cow.starter.appointment.models.AppointmentCreateDTO;
import cow.starter.appointment.models.AppointmentDTO;
import cow.starter.appointment_request.models.*;
import cow.starter.bovine.models.Bovine;
import cow.starter.bovine.models.BovineRepository;
import cow.starter.user.models.User;
import cow.starter.user.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentRequestService {

    @Autowired
    AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineRepository bovineRepository;

    @Autowired
    AppointmentService appointmentService;

    public AppointmentRequestDTO convertToDTO(AppointmentRequest appointmentRequest) {
        return new AppointmentRequestDTO(appointmentRequest.getIdAppointmentRequest(), appointmentRequest.getUser().getIdUser(),
                appointmentRequest.getUserRequest().getIdUser(), appointmentRequest.getBovine().getIdBovine(),
                appointmentRequest.getAppointmentRequestDate().toString(), appointmentRequest.getMotive(),
                appointmentRequest.getAppointmentRequestStatus());
    }

    public AppointmentRequestFullInfoDTO convertFullInfoToDTO(AppointmentRequest appointmentRequest, String userName,
                                              String userRequestName, long serialNumber) {
        return new AppointmentRequestFullInfoDTO(appointmentRequest.getIdAppointmentRequest(),
                appointmentRequest.getUser().getIdUser(), appointmentRequest.getUserRequest().getIdUser(),
                appointmentRequest.getBovine().getIdBovine(), appointmentRequest.getAppointmentRequestDate().toString(),
                appointmentRequest.getMotive(), appointmentRequest.getAppointmentRequestStatus(), userName, userRequestName, serialNumber);
    }

    public List<AppointmentRequestFullInfoDTO> getAppointmentsRequest(List<AppointmentRequest> appointmentRequestList){
        List<AppointmentRequestFullInfoDTO> appointmentRequestDTOList = new ArrayList<>();
        if (!appointmentRequestList.isEmpty()) {
            for (AppointmentRequest appointmentRequest : appointmentRequestList) {
                User userVeterinary = userRepository.getUser(appointmentRequest.getUser().getIdUser());
                User user = userRepository.getUser(appointmentRequest.getUser().getIdUser());
                Bovine bovine = bovineRepository.getBovine(appointmentRequest.getBovine().getIdBovine());
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
            List<Bovine> bovines =  new ArrayList<>();
            for ( long i : appointmentRequestCreateDTO.getBovineIds() ){
                Bovine bovine = bovineRepository.getBovine(i);
                bovines.add(bovine);
            }

            for (Bovine bovine: bovines) {
                AppointmentRequest appointmentRequest = appointmentRequestRepository.checkAppointmentRequest(
                        bovine.getIdBovine(), appointmentRequestCreateDTO.getAppointmentDate());
                if (appointmentRequest == null && checkAppointmentRequestValues(appointmentRequestCreateDTO.getIdUser(),
                        appointmentRequestCreateDTO.getIdUserRequest(), bovine.getIdBovine())) {

                    AppointmentRequest newAppointmentRequest = new AppointmentRequest(
                            userRepository.getUser(appointmentRequestCreateDTO.getIdUser()),
                            userRepository.getUser(appointmentRequestCreateDTO.getIdUserRequest()),
                            bovine,
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
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Date appointmentDate = formatter.parse(appointmentRequestDTO.getAppointmentDate());

                AppointmentRequest appointmentRequest = appointmentRequestRepository.getAppointmentRequest(appointmentRequestDTO.getIdAppointmentRequest());
                if (appointmentRequest != null) {
                    appointmentRequest.setBovine(bovineRepository.getBovine(appointmentRequestDTO.getIdBovine()));
                    appointmentRequest.setUser(userRepository.getUser(appointmentRequestDTO.getIdUser()));
                    appointmentRequest.setAppointmentRequestDate( appointmentDate );
                    appointmentRequest.setMotive(appointmentRequestDTO.getMotive());
                    appointmentRequest.setAppointmentRequestStatus(appointmentRequestDTO.getStatus());
                    appointmentRequestRepository.save(appointmentRequest);
                    return convertToDTO(appointmentRequest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emptyAppointmentRequestDTO;
    }

    public AppointmentRequestDTO updateAppointmentRequestStatus(long idAppointmentRequest, int status) {
        try {
            AppointmentRequest appointmentRequest = appointmentRequestRepository.getAppointmentRequest(idAppointmentRequest);
            if (appointmentRequest != null) {
                appointmentRequest.setAppointmentRequestStatus(status);
                appointmentRequestRepository.save(appointmentRequest);

                    if (status == 1) {
                        ArrayList<Integer> bovineIds = new ArrayList<>();
                        bovineIds.add((int) appointmentRequest.getBovine().getIdBovine());
                        AppointmentCreateDTO appointmentCreateDTO = new AppointmentCreateDTO(
                                appointmentRequest.getIdAppointmentRequest(), appointmentRequest.getUser().getIdUser(),
                                appointmentRequest.getAppointmentRequestDate(), appointmentRequest.getMotive(), 0.0,
                                "", 0, bovineIds);
                        AppointmentDTO appointmentDTO = appointmentService.createAppointment(appointmentCreateDTO);
                        if (appointmentDTO.getIdAppointment() != 0) {
                            return convertToDTO(appointmentRequest);
                        }
                }
                return convertToDTO(appointmentRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AppointmentRequestDTO();
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
