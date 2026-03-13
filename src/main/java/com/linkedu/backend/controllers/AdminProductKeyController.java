package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.ProductKey;
import com.linkedu.backend.services.ProductKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/product-keys")
@RequiredArgsConstructor
public class AdminProductKeyController {

    private final ProductKeyService productKeyService;

    @PostMapping
    public ResponseEntity<?> createProductKey(@RequestBody String keyValue) {  // ← Simple String!
        ProductKey key = productKeyService.createKey(keyValue.trim());
        return ResponseEntity.ok(Map.of("productKeyId", key.getId(), "key", key.getKeyValue()));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableKeys() {
        return ResponseEntity.ok(productKeyService.getAvailableKeys());
    }
}
