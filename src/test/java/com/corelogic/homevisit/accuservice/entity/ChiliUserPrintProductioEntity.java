package com.corelogic.homevisit.accuservice.entity;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chiliUserPrintProduction")
public class ChiliUserPrintProductioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "upid")
    private Long upId;

    @Column(name = "thumbURL")
    private String thumbURL;


}
