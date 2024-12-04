package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shopping_cart_id")
    private List<Product> shoppingCartItems = new ArrayList<>();
    private double totalPrice = 0;

}
