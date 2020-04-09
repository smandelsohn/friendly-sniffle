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
@Table(name = "chiliMasterTemplateAltLayout")
public class ChiliMasterTemplateAltLayoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mtID")
    private Long mtID;


    @Column(name = "postagePlacement")
    private Long postagePlacement;



    @Column(name = "name")
    private String name;

    @Column(name = "itemID")
    private String itemID;

}
