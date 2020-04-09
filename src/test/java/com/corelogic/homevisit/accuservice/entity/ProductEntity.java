package com.corelogic.homevisit.accuservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
@EqualsAndHashCode(exclude = {"item", "cartId"})
@ToString(exclude = {"item", "cartId"})
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer qty;

    @ManyToOne
    @JoinColumn(name = "cartID")
    private CartEntity cartId;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "selection")
    private ItemEntity item;


    @Column(name = "chili_print_id")
    private Long chiliPrintId;

}
