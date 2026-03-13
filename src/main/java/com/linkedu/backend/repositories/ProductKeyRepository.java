package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.ProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductKeyRepository extends JpaRepository<ProductKey, Long> {
    Optional<ProductKey> findByKeyValue(String keyValue);
    List<ProductKey> findByUsedFalse();  // Available keys
}
