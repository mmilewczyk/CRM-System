package com.agiklo.oracledatabase.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PURCHASES_POSITIONS")
public class PurchasesPositions {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "purchases_positions_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    @Column(name = "PURCHASE_NUMBER")
    private Long id;

    @OneToOne
    @JoinColumn(name = "PURCHASE_ID", nullable = false)
    private Purchases purchases;

    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private Double amount;
    private Character reclamationExist;

    public PurchasesPositions(Purchases purchases,
                              Product product,
                              Double amount,
                              Character reclamationExist) {
        this.purchases = purchases;
        this.product = product;
        this.amount = amount;
        this.reclamationExist = reclamationExist;
    }
}
