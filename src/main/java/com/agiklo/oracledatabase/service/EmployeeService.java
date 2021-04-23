package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.dto.EmployeeDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportEmployeeToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportEmployeeToXLSX;
import com.agiklo.oracledatabase.mapper.EmployeeMapper;
import com.agiklo.oracledatabase.security.Registration.token.ConfirmationToken;
import com.agiklo.oracledatabase.security.Registration.token.ConfirmationTokenService;
import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.repository.EmployeeRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class EmployeeService implements UserDetailsService, CurrentTimeInterface {

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmployeeMapper employeeMapper;

    private static final String USER_NOT_FOUND_MSG =
            "user with email %s not found";


    /**
     * @param email email of the user
     * @return user found via email
     * @throws UsernameNotFoundException if user does not exist in database
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    /**
     * The method checks if the user with the given e-mail already exists in the database,
     * if it exists, it throws an exception. However, if the user does not exist in the database,
     * the method adds the user and creates a token that is needfor confirmation.
     * @param employee requestbody of the employee to be saved
     * @throws IllegalStateException if email already exists in the database
     *
     * @return token needed for enable account
     */
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
                employee);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    /**
     * The task of the method is enable user in the database after confirming the account
     * @param email email of the user
     * @return enable user account
     */
    public int enableUser(String email) {
        return employeeRepository.enableUser(email);
    }

    /**
     * The method is to retrieve all employee from the database and display them.
     *
     * After downloading all the data about the employee,
     * the data is mapped to dto which will display only those needed
     * @return list of all employees with specification of data in EmployeeDTO
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees(Pageable pageable){
        return employeeRepository.findAllBy(pageable)
                .stream()
                .map(employeeMapper::mapEmployeeToDto)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific employee from the database and display it.
     * After downloading all the data about the employee,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the employee to be searched for
     * @throws ResponseStatusException if the id of the employee you are looking for does not exist throws 404 status
     * @return detailed data about a specific employee
     */
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee cannot be found, the specified id does not exist"));
        return employeeMapper.mapEmployeeToDto(employee);
    }

    /**
     * The method is to retrieve employee whose have the firstname specified by the user.
     * After downloading all the data about the employee,
     * the data is mapped to dto which will display only those needed
     * @param firstName firstname of the employee
     * @return details of specific employee
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findEmployeesByFirstname(String firstName, Pageable pageable) {
        return employeeRepository.findUserByFirstNameContainingIgnoreCase(firstName, pageable)
                .stream()
                .map(employeeMapper::mapEmployeeToDto)
                .collect(Collectors.toList());
    }

    /**
     * Method deletes the selected employee by id
     * @param id id of the employee to be deleted
     * @throws ResponseStatusException if id of the employee is incorrect throws 404 status with message
     */
    public void deleteEmployeeById(Long id) throws NotFoundException {
        try{
            employeeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    /**
     * The purpose of the method is to set the details of the
     * excel file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Employee> employeeList = employeeRepository.findAll();

        ExportEmployeeToXLSX exporter = new ExportEmployeeToXLSX(employeeList);
        exporter.export(response);
    }

    /**
     * The purpose of the method is to set the details of the
     * pdf file that will be exported for download and then download it.
     * @param response response to determine the details of the file
     * @throws IOException if incorrect data is sent to the file
     */
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Employee> listEmployees = employeeRepository.findAll();

        ExportEmployeeToPDF exporter = new ExportEmployeeToPDF(listEmployees);
        exporter.export(response);
    }

}
