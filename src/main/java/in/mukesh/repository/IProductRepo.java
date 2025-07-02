package in.mukesh.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.mukesh.entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepo extends JpaRepository<ProductEntity, Long> {

	List<ProductEntity> findByCategoryIgnoreCase(String category);

	Page<ProductEntity> findByCategoryIgnoreCase(String category, Pageable pageable);

	List<ProductEntity> findByCreatedBy(String createdBy);


}
