package com.agiklo.oracledatabase.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Customers {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "customer_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    @Column(name = "CUSTOMER_ID", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(length = 11)
    private String pesel;
    @Column(name = "ZIP_CODE")
    private String zipCode;
    private String city;

    public Customers(String firstname, String lastname, String pesel, String zipCode, String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.pesel = pesel;
        this.zipCode = zipCode;
        this.city = city;
    }
}
