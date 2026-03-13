package com.linkedu.backend.services;

import com.linkedu.backend.entities.ProductKey;
import com.linkedu.backend.repositories.ProductKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductKeyService {

    private final ProductKeyRepository productKeyRepository;

    public ProductKey createKey(String keyValue) {
        if (productKeyRepository.findByKeyValue(keyValue).isPresent()) {
            throw new RuntimeException("Product key already exists");
        }

        ProductKey key = new ProductKey();
        key.setKeyValue(keyValue);
        key.setUsed(false);
        key.setCreatedAt(LocalDateTime.now());
        return productKeyRepository.save(key);
    }

    public List<ProductKey> getAvailableKeys() {
        return productKeyRepository.findByUsedFalse();
    }
}
