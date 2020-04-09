package com.corelogic.homevisit.accuservice.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chiliUserPrintProduction")
@SuppressWarnings({"PMD.TooManyFields", "checkstyle:MagicNumber"})

public class ChiliUserPrintProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "print_request_id")
    private Long printRequest;


    @Column(name = "printReadyURL")
    private String printReadyURL;




}
