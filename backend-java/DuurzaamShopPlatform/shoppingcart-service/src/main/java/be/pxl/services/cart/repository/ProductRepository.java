package be.pxl.services.cart.repository;

import be.pxl.services.cart.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
