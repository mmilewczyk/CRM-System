package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.dto.EmployeeDTO;
import com.agiklo.oracledatabase.exports.ExportEmployeeToPDF;
import com.agiklo.oracledatabase.mapper.EmployeeMapper;
import com.agiklo.oracledatabase.security.Registration.token.ConfirmationToken;
import com.agiklo.oracledatabase.security.Registration.token.ConfirmationTokenService;
import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.repository.EmployeeRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmployeeMapper employeeMapper;

    private static final String USER_NOT_FOUND_MSG =
            "user with email %s not found";


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(Employee employee){
        boolean userExists = employeeRepository.findByEmail(employee.getEmail()).isPresent();
        if (userExists){
            //TODO: IF USER NOT CONFIRMED, SEND EMAIL AGAIN
            throw new IllegalStateException(
                    String.format("Email %s already taken", employee.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        employeeRepository.save(employee);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                employee
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return employeeRepository.enableUser(email);
    }

    //TODO: IMPLEMENT THIS METHOD
    public Optional<Employee> findEmployeeByEmail(String email){
        return employeeRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees(){
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::mapEmployeeToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee cannot be found, the specified id does not exist"));
        return employeeMapper.mapEmployeeToDto(employee);
    }

    @Transactional(readOnly = true)
    public Set<EmployeeDTO> findEmployeesByFirstname(String firstName) {
        return employeeRepository.findUserByFirstNameLike(firstName)
                .stream()
                .map(employeeMapper::mapEmployeeToDto)
                .collect(Collectors.toSet());
    }

    public void deleteEmployeeById(Long id) throws NotFoundException {
        try{
            employeeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Employee> listEmployees = employeeRepository.findAll();

        ExportEmployeeToPDF exporter = new ExportEmployeeToPDF(listEmployees);
        exporter.export(response);
    }

}
