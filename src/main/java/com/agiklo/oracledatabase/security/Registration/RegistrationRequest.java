package com.agiklo.oracledatabase.security.Registration;

import com.agiklo.oracledatabase.entity.Departments;
import lombok.*;

import java.time.LocalDate;

/**
 * the class contains the fields that are required in the registration form
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String pesel;
    private final String sex;
    private final LocalDate birthdate;
    private final Double salary;
    private final Departments department;
}
