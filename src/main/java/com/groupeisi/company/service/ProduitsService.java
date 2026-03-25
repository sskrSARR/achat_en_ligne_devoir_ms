package com.groupeisi.company.service;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
import com.groupeisi.company.exception.ResourceNotFoundException;
import com.groupeisi.company.mapper.ProduitsMapper;
import com.groupeisi.company.repository.ProduitsRepository;
import com.groupeisi.company.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProduitsService {

    private final ProduitsRepository produitsRepository;
    private final UserAccountRepository userAccountRepository;
    private final ProduitsMapper produitsMapper;

    @Cacheable(value = "produits")
    @Transactional(readOnly = true)
    public List<ProduitsDto> findAll() {
        log.info("Récupération de tous les produits");
        return produitsMapper.toDtoList(produitsRepository.findAll());
    }

    @Cacheable(value = "produit", key = "#ref")
    @Transactional(readOnly = true)
    public ProduitsDto findByRef(String ref) {
        log.info("Récupération du produit avec la référence : {}", ref);
        Produits produit = produitsRepository.findById(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec la référence : " + ref));
        return produitsMapper.toDto(produit);
    }

    @Transactional(readOnly = true)
    public List<ProduitsDto> findByUserId(Long userId) {
        log.info("Récupération des produits pour l'utilisateur : {}", userId);
        return produitsMapper.toDtoList(produitsRepository.findByUserId(userId));
    }

    @CacheEvict(value = "produits", allEntries = true)
    public ProduitsDto create(ProduitsDto dto) {
        log.info("Création d'un nouveau produit : {}", dto.getName());

        UserAccount user = userAccountRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + dto.getUserId()));

        Produits produit = produitsMapper.toEntity(dto);
        produit.setUser(user);
        // ref == null => @UuidGenerator le génère automatiquement
        Produits saved = produitsRepository.save(produit);
        log.info("Produit créé avec succès, ref : {}", saved.getRef());
        return produitsMapper.toDto(saved);
    }

    @CachePut(value = "produit", key = "#ref")
    @CacheEvict(value = "produits", allEntries = true)
    public ProduitsDto update(String ref, ProduitsDto dto) {
        log.info("Mise à jour du produit avec la référence : {}", ref);
        Produits existing = produitsRepository.findById(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec la référence : " + ref));

        if (dto.getUserId() != null) {
            UserAccount user = userAccountRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + dto.getUserId()));
            existing.setUser(user);
        }

        produitsMapper.updateFromDto(dto, existing);
        Produits updated = produitsRepository.save(existing);
        log.info("Produit mis à jour avec succès, ref : {}", updated.getRef());
        return produitsMapper.toDto(updated);
    }

    @CacheEvict(value = {"produit", "produits"}, allEntries = true)
    public void delete(String ref) {
        log.info("Suppression du produit avec la référence : {}", ref);
        if (!produitsRepository.existsById(ref)) {
            throw new ResourceNotFoundException("Produit non trouvé avec la référence : " + ref);
        }
        produitsRepository.deleteById(ref);
        log.info("Produit supprimé avec succès, ref : {}", ref);
    }
}
