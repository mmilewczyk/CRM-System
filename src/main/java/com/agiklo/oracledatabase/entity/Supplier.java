package com.agiklo.oracledatabase.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Supplier {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "supplier_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    private Long supplierId;
    private String supplierName;

    @Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "MODE_OF_TRANSPORT_CODE")
    private TypesOfTransport modeOfTransportCode;

    private String activityStatus;

    public Supplier(String supplierName,
                    TypesOfTransport modeOfTransportCode,
                    String activityStatus) {
        this.supplierName = supplierName;
        this.modeOfTransportCode = modeOfTransportCode;
        this.activityStatus = activityStatus;
    }
}
