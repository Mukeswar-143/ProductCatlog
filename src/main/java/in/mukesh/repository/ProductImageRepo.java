package in.mukesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.mukesh.entity.ProductImage;

public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {
}
