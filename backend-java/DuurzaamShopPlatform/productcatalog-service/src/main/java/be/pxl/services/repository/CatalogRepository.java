package be.pxl.services.repository;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Product,Long> {
    List<Product> findProductsByCategory(Category category);

    List<Product> findProductsByLabel(String label);
}
