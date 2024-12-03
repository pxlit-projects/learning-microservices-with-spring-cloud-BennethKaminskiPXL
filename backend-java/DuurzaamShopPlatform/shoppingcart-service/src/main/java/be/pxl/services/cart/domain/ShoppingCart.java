package be.pxl.services.cart.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data //setters, getters, equesls, hashcode, tostring
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shoppingcarts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Transient
    private List<Product> shoppingCartItems;
    private double totalPrice = 0;


}
