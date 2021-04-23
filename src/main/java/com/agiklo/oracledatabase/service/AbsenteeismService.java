package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.entity.dto.AbsenteeismDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportAbsenteeismToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportAbsenteeismToXLSX;
import com.agiklo.oracledatabase.mapper.AbsenteeismMapper;
import com.agiklo.oracledatabase.repository.AbsenteeismRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class AbsenteeismService implements CurrentTimeInterface{

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final AbsenteeismRepository absenteeismRepository;
    private final AbsenteeismMapper absenteeismMapper;

    /**
     * The method is to retrieve all absenteeisms from the database and display them.
     *
     * After downloading all the data about the absenteeism,
     * the data is mapped to dto which will display only those needed
     * @return list of all absenteeisms with specification of data in AbsenteeismsDTO
     */
    @Transactional(readOnly = true)
    public List<AbsenteeismDTO> getAllAbsenteeisms(Pageable pageable){
        return absenteeismRepository.findAllBy(pageable)
                .stream()
                .map(absenteeismMapper::mapAbsenteeismToDto)
                .collect(Collectors.toList());
    }

    /**
     * The method is to download a specific absenteeism from the database and display it.
     * After downloading all the data about the absenteeism,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the absenteeism to be searched for
     * @throws ResponseStatusException if the id of the absenteeism you are looking for does not exist
     * @return detailed data about a specific absenteeism
     */
    @Transactional(readOnly = true)
    public AbsenteeismDTO getAbsenteeismById(Long id){
        Absenteeism absenteeism = absenteeismRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Absenteeism cannot be found, the specified id does not exist"));
        return absenteeismMapper.mapAbsenteeismToDto(absenteeism);
    }

    /**
     * The task of the method is to add a absenteeism to the database.
     * @param absenteeism requestbody of the absenteeism to be saved
     * @return saving the absenteeism to the database
     */
    public Absenteeism addNewAbsenteeism(Absenteeism absenteeism) {
        return absenteeismRepository.save(absenteeism);
    }

    /**
     * Method deletes the selected absenteeism by id
     * @param id id of the absenteeism to be deleted
     * @throws ResponseStatusException if id of the absenteeism is incorrect throws 404 status with message
     */
    public void deleteAbsenteeismById(Long id) {
        try{
            absenteeismRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Absenteeism cannot be found, the specified id does not exist");
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
        String headerValue = "attachment; filename=absenteeisms_" + getCurrentDateTime() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Absenteeism> absenteeismList = absenteeismRepository.findAll();

        ExportAbsenteeismToXLSX exporter = new ExportAbsenteeismToXLSX(absenteeismList);
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
        String headerValue = "attachment; filename=absenteeisms_" + getCurrentDateTime() + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Absenteeism> absenteeismList = absenteeismRepository.findAll();

        ExportAbsenteeismToPDF exporter = new ExportAbsenteeismToPDF(absenteeismList);
        exporter.export(response);
    }
}
