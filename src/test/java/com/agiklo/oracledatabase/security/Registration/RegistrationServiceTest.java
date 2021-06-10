package com.agiklo.oracledatabase.security.Registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.entity.Departments;
import com.agiklo.oracledatabase.security.Registration.token.ConfirmationTokenService;
import com.agiklo.oracledatabase.security.email.EmailSender;
import com.agiklo.oracledatabase.service.EmployeeService;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmployeeService.class, RegistrationService.class, EmailValidator.class,
        ConfirmationTokenService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class RegistrationServiceTest {
    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private EmailValidator emailValidator;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private RegistrationService registrationService;

    @Test
    void shouldReturnToken() {
        // when
        when(this.employeeService.signUpUser(any())).thenReturn("token");
        when(this.emailValidator.test(anyString())).thenReturn(true);
        doNothing().when(this.emailSender).send(anyString(), anyString());
        LocalDate birthdate = LocalDate.ofEpochDay(1L);

        // then
        assertEquals("token", this.registrationService.register(new RegistrationRequest(
                "Mateusz",
                "Milewczyk",
                "mateusz@agiklocrm.com",
                "test",
                "Pesel",
                "Male",
                birthdate,
                5_000.00,
                new Departments())));

        verify(this.emailSender).send(anyString(), anyString());
        verify(this.emailValidator).test(anyString());
        verify(this.employeeService).signUpUser(any());
    }

    @Test
    void shouldNotRegisterIfTestReturnsFalseAndThrowException() {
        // when
        when(this.employeeService.signUpUser(any())).thenReturn("token");
        when(this.emailValidator.test(anyString())).thenReturn(false);
        doNothing().when(this.emailSender).send(anyString(), anyString());
        LocalDate birthdate = LocalDate.ofEpochDay(1L);

        // then
        assertThrows(IllegalStateException.class,
                () -> this.registrationService.register(new RegistrationRequest(
                "Mateusz",
                "Milewczyk",
                "mateusz@agiklocrm.com",
                "test",
                "Pesel",
                "Male",
                birthdate,
                5_000.00,
                new Departments())));

        verify(this.emailValidator).test(anyString());
    }
}

