package com.corelogic.homevisit.accuservice.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agvproductvariant")
public class AgvProductVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;


    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "print_name")
    private String printName;


}
