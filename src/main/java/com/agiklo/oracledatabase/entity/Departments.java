package com.agiklo.oracledatabase.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "DEPARTMENTS")
public class Departments {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "departments_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    @Column(name = "DEPARTMENT_CODE")
    private Long id;

    private String departmentName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "MANAGER_ID")
    //@JsonBackReference
    private Employee managers;

    private String city;

    public Departments(String departmentName, Employee managers, String city) {
        this.departmentName = departmentName;
        this.managers = managers;
        this.city = city;
    }
}
