package com.corelogic.homevisit.accuservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chiliDefPostagePlacement")
public class ChiliDefPostagePlacementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postagePlacement")
    private Long postagePlacement;



    @Column(name = "placement")
    private String placement;

}
