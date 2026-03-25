package com.groupeisi.company.service;

import com.groupeisi.company.dto.UserAccountDto;
import com.groupeisi.company.entities.UserAccount;
import com.groupeisi.company.exception.DuplicateResourceException;
import com.groupeisi.company.exception.ResourceNotFoundException;
import com.groupeisi.company.mapper.UserAccountMapper;
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
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    @Cacheable(value = "userAccounts")
    @Transactional(readOnly = true)
    public List<UserAccountDto> findAll() {
        log.info("Récupération de tous les comptes utilisateurs");
        return userAccountMapper.toDtoList(userAccountRepository.findAll());
    }

    @Cacheable(value = "userAccount", key = "#id")
    @Transactional(readOnly = true)
    public UserAccountDto findById(Long id) {
        log.info("Récupération du compte utilisateur avec l'id : {}", id);
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte utilisateur non trouvé avec l'id : " + id));
        return userAccountMapper.toDto(userAccount);
    }

    @Transactional(readOnly = true)
    public UserAccountDto findByEmail(String email) {
        log.info("Récupération du compte utilisateur avec l'email : {}", email);
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Compte utilisateur non trouvé avec l'email : " + email));
        return userAccountMapper.toDto(userAccount);
    }

    @CacheEvict(value = "userAccounts", allEntries = true)
    public UserAccountDto create(UserAccountDto dto) {
        log.info("Création d'un nouveau compte utilisateur : {}", dto.getEmail());
        if (userAccountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Un compte avec cet email existe déjà : " + dto.getEmail());
        }
        UserAccount userAccount = userAccountMapper.toEntity(dto);
        UserAccount saved = userAccountRepository.save(userAccount);
        log.info("Compte utilisateur créé avec succès, id : {}", saved.getId());
        return userAccountMapper.toDto(saved);
    }

    @CachePut(value = "userAccount", key = "#id")
    @CacheEvict(value = "userAccounts", allEntries = true)
    public UserAccountDto update(Long id, UserAccountDto dto) {
        log.info("Mise à jour du compte utilisateur avec l'id : {}", id);
        UserAccount existing = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte utilisateur non trouvé avec l'id : " + id));

        if (!existing.getEmail().equals(dto.getEmail()) && userAccountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Un compte avec cet email existe déjà : " + dto.getEmail());
        }

        userAccountMapper.updateFromDto(dto, existing);
        UserAccount updated = userAccountRepository.save(existing);
        log.info("Compte utilisateur mis à jour avec succès, id : {}", updated.getId());
        return userAccountMapper.toDto(updated);
    }

    @CacheEvict(value = {"userAccount", "userAccounts"}, allEntries = true)
    public void delete(Long id) {
        log.info("Suppression du compte utilisateur avec l'id : {}", id);
        if (!userAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Compte utilisateur non trouvé avec l'id : " + id);
        }
        userAccountRepository.deleteById(id);
        log.info("Compte utilisateur supprimé avec succès, id : {}", id);
    }
}
