package com.groupeisi.company.controller;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.service.VentesService;
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
@RequestMapping("/api/v1/ventes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ventes", description = "API de gestion des ventes")
public class VentesController {

    private final VentesService ventesService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les ventes")
    public ResponseEntity<List<VentesDto>> findAll() {
        log.info("GET /api/v1/ventes");
        return ResponseEntity.ok(ventesService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une vente par son ID")
    public ResponseEntity<VentesDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/ventes/{}", id);
        return ResponseEntity.ok(ventesService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les ventes d'un utilisateur")
    public ResponseEntity<List<VentesDto>> findByUserId(@PathVariable Long userId) {
        log.info("GET /api/v1/ventes/user/{}", userId);
        return ResponseEntity.ok(ventesService.findByUserId(userId));
    }

    @GetMapping("/produit/{productId}")
    @Operation(summary = "Récupérer les ventes d'un produit")
    public ResponseEntity<List<VentesDto>> findByProductId(@PathVariable String productRef) {
        log.info("GET /api/v1/ventes/produit/{}", productRef);
        return ResponseEntity.ok(ventesService.findByProductRef(productRef));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle vente")
    public ResponseEntity<VentesDto> create(@Valid @RequestBody VentesDto dto) {
        log.info("POST /api/v1/ventes - userId : {}, productRef : {}", dto.getUserId(), dto.getProductRef());
        return ResponseEntity.status(HttpStatus.CREATED).body(ventesService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une vente")
    public ResponseEntity<VentesDto> update(@PathVariable Long id, @Valid @RequestBody VentesDto dto) {
        log.info("PUT /api/v1/ventes/{}", id);
        return ResponseEntity.ok(ventesService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une vente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/ventes/{}", id);
        ventesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
