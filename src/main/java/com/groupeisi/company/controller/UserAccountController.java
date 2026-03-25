package com.groupeisi.company.controller;

import com.groupeisi.company.dto.UserAccountDto;
import com.groupeisi.company.service.UserAccountService;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "UserAccount", description = "API de gestion des comptes utilisateurs")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs")
    public ResponseEntity<List<UserAccountDto>> findAll() {
        log.info("GET /api/v1/users");
        return ResponseEntity.ok(userAccountService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par son ID")
    public ResponseEntity<UserAccountDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/users/{}", id);
        return ResponseEntity.ok(userAccountService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Récupérer un utilisateur par son email")
    public ResponseEntity<UserAccountDto> findByEmail(@PathVariable String email) {
        log.info("GET /api/v1/users/email/{}", email);
        return ResponseEntity.ok(userAccountService.findByEmail(email));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau compte utilisateur")
    public ResponseEntity<UserAccountDto> create(@Valid @RequestBody UserAccountDto dto) {
        log.info("POST /api/v1/users - email : {}", dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userAccountService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un compte utilisateur")
    public ResponseEntity<UserAccountDto> update(@PathVariable Long id, @Valid @RequestBody UserAccountDto dto) {
        log.info("PUT /api/v1/users/{}", id);
        return ResponseEntity.ok(userAccountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un compte utilisateur")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/users/{}", id);
        userAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
