package com.agiklo.oracledatabase.entity;

import com.agiklo.oracledatabase.enums.UNITS_OF_MEASURE;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PRODUCT_UNITS")
public class ProductUnits {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "product_units_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "UOM")
    private UNITS_OF_MEASURE unitOfMeasure;

    @Column(name = "UOM_ALT")
    private String alternativeUnitOfMeasure;

    private Double conversionFactor;

    public ProductUnits(Product product,
                        UNITS_OF_MEASURE unitOfMeasure,
                        String alternativeUnitOfMeasure,
                        Double conversionFactor) {
        this.product = product;
        this.unitOfMeasure = unitOfMeasure;
        this.alternativeUnitOfMeasure = alternativeUnitOfMeasure;
        this.conversionFactor = conversionFactor;
    }
}
