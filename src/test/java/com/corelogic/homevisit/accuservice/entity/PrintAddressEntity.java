package com.corelogic.homevisit.accuservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "printAddress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("PMD.TooManyFields")


public class PrintAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long printRequestId;

    private String occupant;
    private String address1;
    private String address2;
    private String city;
    private String zip;


    private String state;

    private boolean certified;

    private String addressType;

    private String addressName;
    private String addressee;
    private String company;
    private String locality;
    private String firstName;
    private String lastName;
}
