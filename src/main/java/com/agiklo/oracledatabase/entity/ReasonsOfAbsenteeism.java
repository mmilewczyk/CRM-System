package com.agiklo.oracledatabase.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "REASONS_OF_ABSENTEEISM")
public class ReasonsOfAbsenteeism {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_generator"
    )
    @SequenceGenerator(
            name="sequence_generator",
            sequenceName = "reasons_of_absenteeism_sequence",
            allocationSize = 1,
            initialValue = 100
    )
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String absenteeismName;
    private Character consent;
    private String comments;

    public ReasonsOfAbsenteeism(String absenteeismName,
                                Character consent,
                                String comments) {
        this.absenteeismName = absenteeismName;
        this.consent = consent;
        this.comments = comments;
    }
}
