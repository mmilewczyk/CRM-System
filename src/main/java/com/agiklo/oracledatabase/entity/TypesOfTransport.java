package com.agiklo.oracledatabase.entity;

import com.agiklo.oracledatabase.enums.MODE_OF_TRANSPORT_CODE;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TYPES_OF_TRANSPORT")
public class TypesOfTransport {

    @Id
    @Enumerated(EnumType.STRING)
    private MODE_OF_TRANSPORT_CODE code;
    private String fullName;
    private Double minLength;
    private Double maxLength;
    private Double minWeight;
    private Double maxWeight;
    private Integer transportCapacity;

}
