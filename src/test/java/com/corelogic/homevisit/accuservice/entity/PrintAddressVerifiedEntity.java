package com.corelogic.homevisit.accuservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "printAddressVerified")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("PMD.TooManyFields")


public class PrintAddressVerifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "print_request_id")
    private Long printRequestId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;


    private String address;
    private String address2;

    private String city;
    private String state;
    private String zip;


    private String sal;
    private String crrt;

    private String barcode;

    private String imbarcode;

    private String imbdigits;
    private String sequenc;
    private String cont_id;
    private String gpb_id;
    private String endorse;

}
