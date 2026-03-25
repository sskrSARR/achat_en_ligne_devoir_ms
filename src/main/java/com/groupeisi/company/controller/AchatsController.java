package com.groupeisi.company.controller;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.service.AchatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achats")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Achats", description = "API de gestion des achats")
public class AchatsController {

    private final AchatsService achatsService;

    @GetMapping
    @Operation(summary = "Récupérer tous les achats")
    public ResponseEntity<List<AchatsDto>> findAll() {
        return ResponseEntity.ok(achatsService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un achat par son ID")
    public ResponseEntity<AchatsDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(achatsService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les achats d'un utilisateur")
    public ResponseEntity<List<AchatsDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(achatsService.findByUserId(userId));
    }

    @GetMapping("/produit/{productRef}")
    @Operation(summary = "Récupérer les achats d'un produit par sa référence UUID")
    public ResponseEntity<List<AchatsDto>> findByProductRef(@PathVariable String productRef) {
        return ResponseEntity.ok(achatsService.findByProductRef(productRef));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel achat (décrémente le stock automatiquement)")
    public ResponseEntity<AchatsDto> create(@Valid @RequestBody AchatsDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(achatsService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un achat")
    public ResponseEntity<AchatsDto> update(@PathVariable Long id, @Valid @RequestBody AchatsDto dto) {
        return ResponseEntity.ok(achatsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un achat")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        achatsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
