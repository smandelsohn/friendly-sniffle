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
@Table(name = "chiliUserPrintPage")
public class ChiliUserPrintPageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "upid")
    private Long upId;

    @Column(name = "printPagePdf")
    private String thumbURL;

    @Column(name = "selectedLayoutID")
    private String selectedLayoutID;

    @Column(name = "pageNumber")
    private Integer pageNumber;

    @Column(name = "mtID")
    private Long mtID;


}
