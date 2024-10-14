package be.pxl.services.catalog.repository;

import be.pxl.services.catalog.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Product,Long> {
}
