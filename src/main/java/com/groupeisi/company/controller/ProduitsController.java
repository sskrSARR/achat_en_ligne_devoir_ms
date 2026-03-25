package com.groupeisi.company.controller;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.service.ProduitsService;
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
@RequestMapping("/api/v1/produits")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Produits", description = "API de gestion des produits")
public class ProduitsController {

    private final ProduitsService produitsService;

    @GetMapping
    @Operation(summary = "Récupérer tous les produits")
    public ResponseEntity<List<ProduitsDto>> findAll() {
        log.info("GET /api/v1/produits");
        return ResponseEntity.ok(produitsService.findAll());
    }

    @GetMapping("/{ref}")
    @Operation(summary = "Récupérer un produit par sa référence UUID")
    public ResponseEntity<ProduitsDto> findByRef(@PathVariable String ref) {
        log.info("GET /api/v1/produits/{}", ref);
        return ResponseEntity.ok(produitsService.findByRef(ref));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les produits d'un utilisateur")
    public ResponseEntity<List<ProduitsDto>> findByUserId(@PathVariable Long userId) {
        log.info("GET /api/v1/produits/user/{}", userId);
        return ResponseEntity.ok(produitsService.findByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau produit (ref UUID auto-généré)")
    public ResponseEntity<ProduitsDto> create(@Valid @RequestBody ProduitsDto dto) {
        log.info("POST /api/v1/produits - name : {}", dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(produitsService.create(dto));
    }

    @PutMapping("/{ref}")
    @Operation(summary = "Mettre à jour un produit par sa référence UUID")
    public ResponseEntity<ProduitsDto> update(@PathVariable String ref, @Valid @RequestBody ProduitsDto dto) {
        log.info("PUT /api/v1/produits/{}", ref);
        return ResponseEntity.ok(produitsService.update(ref, dto));
    }

    @DeleteMapping("/{ref}")
    @Operation(summary = "Supprimer un produit par sa référence UUID")
    public ResponseEntity<Void> delete(@PathVariable String ref) {
        log.info("DELETE /api/v1/produits/{}", ref);
        produitsService.delete(ref);
        return ResponseEntity.noContent().build();
    }
}
