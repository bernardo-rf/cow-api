package com.bernardo.figueiredo.cow.api.business.appointment.boundary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bernardo.figueiredo.cow.api.business.appointment.dto.Appointment;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import java.util.Date;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @ParameterizedTest
    @ValueSource(strings = {"/appointments/999999", "/appointments/bovine/999999", "/appointments/user/999999"})
    void validRequest_getAppointmentByObjects_OkStatus(String url) throws Exception {

        Appointment appointment = new Appointment();
        appointment.setBovine(new Bovine());
        appointment.setUser(new User());
        appointment.setAppointmentDate(new Date());

        Mockito.when(appointmentService.getAppointmentById(999999)).thenReturn(appointment);
        ResultActions result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/appointments/ABC", "/appointments/bovine/ABC", "/appointments/user/ABC"})
    void invalidRequest_getAppointmentByObject_BadRequestStatus(String url) throws Exception {
        ResultActions result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isBadRequest());
    }
}
