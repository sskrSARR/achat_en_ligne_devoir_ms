package com.groupeisi.company.service;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
import com.groupeisi.company.entities.Ventes;
import com.groupeisi.company.exception.ResourceNotFoundException;
import com.groupeisi.company.mapper.VentesMapper;
import com.groupeisi.company.repository.ProduitsRepository;
import com.groupeisi.company.repository.UserAccountRepository;
import com.groupeisi.company.repository.VentesRepository;
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
public class VentesService {

    private final VentesRepository ventesRepository;
    private final ProduitsRepository produitsRepository;
    private final UserAccountRepository userAccountRepository;
    private final VentesMapper ventesMapper;

    @Cacheable(value = "ventes")
    @Transactional(readOnly = true)
    public List<VentesDto> findAll() {
        log.info("Récupération de toutes les ventes");
        return ventesMapper.toDtoList(ventesRepository.findAll());
    }

    @Cacheable(value = "vente", key = "#id")
    @Transactional(readOnly = true)
    public VentesDto findById(Long id) {
        log.info("Récupération de la vente avec l'id : {}", id);
        Ventes vente = ventesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente non trouvée avec l'id : " + id));
        return ventesMapper.toDto(vente);
    }

    @Transactional(readOnly = true)
    public List<VentesDto> findByUserId(Long userId) {
        log.info("Récupération des ventes pour l'utilisateur : {}", userId);
        return ventesMapper.toDtoList(ventesRepository.findByUserId(userId));
    }

    @Transactional(readOnly = true)
    public List<VentesDto> findByProductRef(String productRef) {
        log.info("Récupération des ventes pour le produit : {}", productRef);
        return ventesMapper.toDtoList(ventesRepository.findByProductRef(productRef));
    }

    @CacheEvict(value = "ventes", allEntries = true)
    public VentesDto create(VentesDto dto) {
        log.info("Création d'une nouvelle vente - produit : {}, utilisateur : {}", dto.getProductRef(), dto.getUserId());

        Produits produit = produitsRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec la référence : " + dto.getProductRef()));

        UserAccount user = userAccountRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + dto.getUserId()));

        if (produit.getStock() < dto.getQuantity()) {
            throw new IllegalArgumentException(
                "Stock insuffisant pour la vente. Disponible : " + produit.getStock() + ", demandé : " + dto.getQuantity());
        }

        produit.setStock(produit.getStock() - dto.getQuantity());
        produitsRepository.save(produit);

        Ventes vente = ventesMapper.toEntity(dto);
        vente.setProduct(produit);
        vente.setUser(user);

        Ventes saved = ventesRepository.save(vente);
        log.info("Vente créée avec succès, id : {}", saved.getId());
        return ventesMapper.toDto(saved);
    }

    @CachePut(value = "vente", key = "#id")
    @CacheEvict(value = "ventes", allEntries = true)
    public VentesDto update(Long id, VentesDto dto) {
        log.info("Mise à jour de la vente avec l'id : {}", id);
        Ventes existing = ventesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente non trouvée avec l'id : " + id));

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

        ventesMapper.updateFromDto(dto, existing);
        Ventes updated = ventesRepository.save(existing);
        log.info("Vente mise à jour avec succès, id : {}", updated.getId());
        return ventesMapper.toDto(updated);
    }

    @CacheEvict(value = {"vente", "ventes"}, allEntries = true)
    public void delete(Long id) {
        log.info("Suppression de la vente avec l'id : {}", id);
        if (!ventesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vente non trouvée avec l'id : " + id);
        }
        ventesRepository.deleteById(id);
        log.info("Vente supprimée avec succès, id : {}", id);
    }
}
