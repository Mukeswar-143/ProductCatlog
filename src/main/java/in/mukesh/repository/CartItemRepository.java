package in.mukesh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.mukesh.entity.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByUsername(String username);
}

