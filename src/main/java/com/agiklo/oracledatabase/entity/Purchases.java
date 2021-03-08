package com.agiklo.oracledatabase.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Purchases {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "purchases_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    @Column(name = "PURCHASE_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customers customer;

    private Character receiptExist;
    private Character invoiceExist;

    @OneToOne
    @JoinColumn(name = "INVOICE_ID")
    private SellingInvoice invoice;

    private Date purchaseDate;

    public Purchases(Customers customer,
                     Character receiptExist,
                     Character invoiceExist,
                     SellingInvoice invoice,
                     Date purchaseDate) {
        this.customer = customer;
        this.receiptExist = receiptExist;
        this.invoiceExist = invoiceExist;
        this.invoice = invoice;
        this.purchaseDate = purchaseDate;
    }
}
