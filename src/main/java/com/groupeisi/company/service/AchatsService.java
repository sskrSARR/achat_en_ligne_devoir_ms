package com.groupeisi.company.service;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.entities.Achats;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
import com.groupeisi.company.exception.ResourceNotFoundException;
import com.groupeisi.company.mapper.AchatsMapper;
import com.groupeisi.company.repository.AchatsRepository;
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
public class AchatsService {

    private final AchatsRepository achatsRepository;
    private final ProduitsRepository produitsRepository;
    private final UserAccountRepository userAccountRepository;
    private final AchatsMapper achatsMapper;

    @Cacheable(value = "achats")
    @Transactional(readOnly = true)
    public List<AchatsDto> findAll() {
        log.info("Récupération de tous les achats");
        return achatsMapper.toDtoList(achatsRepository.findAll());
    }

    @Cacheable(value = "achat", key = "#id")
    @Transactional(readOnly = true)
    public AchatsDto findById(Long id) {
        log.info("Récupération de l'achat avec l'id : {}", id);
        Achats achat = achatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achat non trouvé avec l'id : " + id));
        return achatsMapper.toDto(achat);
    }

    @Transactional(readOnly = true)
    public List<AchatsDto> findByUserId(Long userId) {
        log.info("Récupération des achats pour l'utilisateur : {}", userId);
        return achatsMapper.toDtoList(achatsRepository.findByUserId(userId));
    }

    @Transactional(readOnly = true)
    public List<AchatsDto> findByProductRef(String productRef) {
        log.info("Récupération des achats pour le produit : {}", productRef);
        return achatsMapper.toDtoList(achatsRepository.findByProductRef(productRef));
    }

    @CacheEvict(value = "achats", allEntries = true)
    public AchatsDto create(AchatsDto dto) {
        log.info("Création d'un nouvel achat - produit : {}, utilisateur : {}", dto.getProductRef(), dto.getUserId());

        Produits produit = produitsRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec la référence : " + dto.getProductRef()));

        UserAccount user = userAccountRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + dto.getUserId()));

        if (produit.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException(
                "Stock insuffisant. Disponible : " + produit.getStock() + ", demandé : " + dto.getQuantity());
        }

        produit.setStock(produit.getStock() - dto.getQuantity());
        produitsRepository.save(produit);

        Achats achat = achatsMapper.toEntity(dto);
        achat.setProduct(produit);
        achat.setUser(user);

        Achats saved = achatsRepository.save(achat);
        log.info("Achat créé avec succès, id : {}", saved.getId());
        return achatsMapper.toDto(saved);
    }

    @CachePut(value = "achat", key = "#id")
    @CacheEvict(value = "achats", allEntries = true)
    public AchatsDto update(Long id, AchatsDto dto) {
        log.info("Mise à jour de l'achat avec l'id : {}", id);
        Achats existing = achatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achat non trouvé avec l'id : " + id));

        if (dto.getProductRef() != null) {
            Produits produit = produitsRepository.findById(dto.getProductRef())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé : " + dto.getProductRef()));
            existing.setProduct(produit);
        }
        if (dto.getUserId() != null) {
            UserAccount user = userAccountRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + dto.getUserId()));
            existing.setUser(user);
        }

        achatsMapper.updateFromDto(dto, existing);
        Achats updated = achatsRepository.save(existing);
        log.info("Achat mis à jour avec succès, id : {}", updated.getId());
        return achatsMapper.toDto(updated);
    }

    @CacheEvict(value = {"achat", "achats"}, allEntries = true)
    public void delete(Long id) {
        log.info("Suppression de l'achat avec l'id : {}", id);
        if (!achatsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Achat non trouvé avec l'id : " + id);
        }
        achatsRepository.deleteById(id);
        log.info("Achat supprimé avec succès, id : {}", id);
    }
}
