package com.agiklo.oracledatabase.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Absenteeism {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "absenteeism_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROA_CODE", nullable = false)
    private ReasonsOfAbsenteeism reasonOfAbsenteeismCode;

    @Column(name = "DATE_FROM")
    private Date dateFrom;
    @Column(name = "DATE_TO")
    private Date dateTo;

    public Absenteeism(Employee employee,
                       ReasonsOfAbsenteeism reasonOfAbsenteeismCode,
                       Date dateFrom,
                       Date dateTo) {
        this.employee = employee;
        this.reasonOfAbsenteeismCode = reasonOfAbsenteeismCode;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
