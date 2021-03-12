package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Absenteeism;
import com.agiklo.oracledatabase.entity.dto.AbsenteeismDTO;
import com.agiklo.oracledatabase.exports.pdf.ExportAbsenteeismToPDF;
import com.agiklo.oracledatabase.exports.excel.ExportAbsenteeismToXLSX;
import com.agiklo.oracledatabase.mapper.AbsenteeismMapper;
import com.agiklo.oracledatabase.repository.AbsenteeismRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AbsenteeismService {

    private final AbsenteeismRepository absenteeismRepository;
    private final AbsenteeismMapper absenteeismMapper;

    @Transactional(readOnly = true)
    public List<AbsenteeismDTO> getAllAbsenteeisms(){
        return absenteeismRepository.findAll()
                .stream()
                .map(absenteeismMapper::mapAbsenteeismToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AbsenteeismDTO getAbsenteeismById(Long id){
        Absenteeism absenteeism = absenteeismRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Absenteeism cannot be found, the specified id does not exist"));
        return absenteeismMapper.mapAbsenteeismToDto(absenteeism);
    }

    public Absenteeism addNewAbsenteeism(Absenteeism absenteeism) {
        return absenteeismRepository.save(absenteeism);
    }

    public void deleteAbsenteeismById(Long id) throws NotFoundException {
        try{
            absenteeismRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    public void exportToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.ms-excel");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=absenteeisms_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Absenteeism> absenteeismList = absenteeismRepository.findAll();

        ExportAbsenteeismToXLSX exporter = new ExportAbsenteeismToXLSX(absenteeismList);
        exporter.export(response);
    }


    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=absenteeisms_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Absenteeism> absenteeismList = absenteeismRepository.findAll();

        ExportAbsenteeismToPDF exporter = new ExportAbsenteeismToPDF(absenteeismList);
        exporter.export(response);
    }
}
