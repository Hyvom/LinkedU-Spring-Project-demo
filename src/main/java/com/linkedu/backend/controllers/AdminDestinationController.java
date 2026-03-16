package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.Destination;
import com.linkedu.backend.services.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/destinations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // ← ADMIN ONLY
public class AdminDestinationController {

    private final DestinationService destinationService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestination(@PathVariable Long id) {
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createDestination(@RequestBody Destination destination) {
        Destination saved = destinationService.createDestination(destination);
        return ResponseEntity.ok(Map.of("id", saved.getId(), "message", "Destination created"));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDestination(@PathVariable Long id, @RequestBody Destination destination) {
        Destination updated = destinationService.updateDestination(id, destination);
        return ResponseEntity.ok(Map.of("id", updated.getId(), "message", "Destination updated"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.ok(Map.of("message", "Destination deleted"));
    }
}
