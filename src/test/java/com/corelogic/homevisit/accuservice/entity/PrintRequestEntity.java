package com.corelogic.homevisit.accuservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "printRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String provider;

    private String printType;

    @Column(length = 48, name = "acc_uuid")
    private String accuuid;

    /**
     * NONE (empty) - can pickup 4 verification,
     * CVSUP, REQ ,READY, DONE
     */
    @Column(length = 12, name = "acc_status")
    private String accstatus;

    @Column(name = "acc_count")
    private int acccount;


}
