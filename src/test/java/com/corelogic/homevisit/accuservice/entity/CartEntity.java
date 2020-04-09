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
@ToString(exclude = {"carts", "agent", "home"})
@Table(name = "cart_id")
@SuppressWarnings({"PMD.TooManyFields", "checkstyle:MagicNumber"})
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salesTax")
    private BigDecimal salesTax;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = " totalCost")
    private BigDecimal totalCost;

    @Column(name = "shippingCost")
    private BigDecimal shippingCost;

    @Column(name = "userId")
    private Integer userId;


    @Length(max = 3)
    private String salesTaxState;
    private Integer orderType;

    @Column(name = "complete")
    private Boolean complete;

    @Column(name = "hvID")
    private Long hvId;

    @Column(name = "sAddress")
    private String shippingAddress;
    @Column(name = "sCity")
    private String shippingCity;

    @Column(name = "sState")
    private String shippingState;

    @Column(name = "sAddressID")
    private Long addressId;
    @Column(name = "sCertified")
    private Boolean shippingCertified;

    @Column(name = "sAddressModel")
    private String shippingAddressType;

    @Column(name = "sAddressType")
    private String shippingAddressLocationType;

    @Column(name = "sAddressName")
    private String shippingAddressName;
    @Column(name = "sAddressee")
    private String shippingAddressee;
    @Column(name = "sCompany")
    private String shippingCompany;
    @Column(name = "sLocality")
    private String shippingLocality;
    @Column(name = "sFirstName")
    private String shippingFirstName;
    @Column(name = "sLastName")
    private String shippingLastName;
    @Column(name = "sZip", length = 15)
    private String shippingZip;
    @Column(name = "shipMethod")
    private Long shipMethod;
    @Column(name = "aAddressID")
    private Long billingAddressId; //already added
    @Column(name = "aAddress")
    private String billingAddress;
    @Column(name = "aCity")
    private String billingCity;

    @Column(name = "aState")
    private String billingState;

    @Column(name = "aZip", length = 15)
    private String billingZip;
    @Column(name = "aCertified")
    private Boolean billingCertified;

    @Column(name = "aAddressModel")
    private String billingAddressType;


    @Column(name = "aAddressType")
    private String billingAddressLocationType;

    @Column(name = "aAddressName")
    private String billingAddressName;

    @Column(name = "aAddressee")
    private String billingAddressee;

    @Column(name = "aCompany")
    private String billingCompany;

    @Column(name = "aLocality")
    private String billingLocality;

    @Column(name = "aFirstName")
    private String billingFirstName;

    @Column(name = "aLastName")
    private String billingLastName;

    @Column(name = "pID")
    private Long productionId;


    @Length(max = 50)
    @Column(name = "creditCard")
    private String creditCard;
    @Length(max = 100)
    @Column(name = "creditCardNumber")
    private String creditCardNumber;
    @Length(max = 15)
    @Column(name = "creditCardExp")
    private String creditCardExp;
    @Length(max = 200)
    @Column(name = "creditCardName")
    private String creditCardName;
    private Boolean ccCharged;
    private String ccApproval;
    private String creditCardId;
    private String ccError;
    private String ccAnaTransId;
    private LocalDateTime ccChargedTime;
    @Column(name = "stripe_json")
    private String stripeJson;

    //@OneToOne
    @Column(name = "print_request_id")
    private Long printRequest;

    @Column(name = "orderNotes")
    private String notes;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cartId", orphanRemoval = true)
    private Set<ProductEntity> carts;

}
