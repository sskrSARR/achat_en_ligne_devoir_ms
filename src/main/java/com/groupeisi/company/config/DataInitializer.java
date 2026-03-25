package com.groupeisi.company.config;

import com.groupeisi.company.entities.Achats;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.UserAccount;
import com.groupeisi.company.entities.Ventes;
import com.groupeisi.company.repository.AchatsRepository;
import com.groupeisi.company.repository.ProduitsRepository;
import com.groupeisi.company.repository.UserAccountRepository;
import com.groupeisi.company.repository.VentesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    @Profile("!test")
    public CommandLineRunner initData(
            UserAccountRepository userRepo,
            ProduitsRepository prodRepo,
            AchatsRepository achatsRepo,
            VentesRepository ventesRepo) {

        return args -> {
            // Ne précharger QUE si la table est vide
            if (userRepo.count() > 0) {
                log.info("=== Données déjà présentes, initialisation ignorée ===");
                return;
            }

            log.info("=== Initialisation des données de démarrage ===");

            // Utilisateurs — ref UUID auto-générée par @UuidGenerator
            UserAccount admin = userRepo.save(UserAccount.builder()
                    .email("admin@groupeisi.com")
                    .password("admin123")
                    .build());

            UserAccount user1 = userRepo.save(UserAccount.builder()
                    .email("user1@groupeisi.com")
                    .password("user123")
                    .build());

            log.info("Utilisateurs créés : id={}, id={}", admin.getId(), user1.getId());

            // Produits — ref est l'@Id UUID, on ne le fournit PAS : @UuidGenerator le génère
            Produits p1 = prodRepo.save(Produits.builder()
                    .name("Laptop Dell XPS")
                    .stock(50.0)
                    .user(admin)
                    .build());

            Produits p2 = prodRepo.save(Produits.builder()
                    .name("iPhone 15 Pro")
                    .stock(100.0)
                    .user(admin)
                    .build());

            Produits p3 = prodRepo.save(Produits.builder()
                    .name("Samsung TV 55\"")
                    .stock(25.0)
                    .user(user1)
                    .build());

            log.info("Produits créés : ref={}, ref={}, ref={}", p1.getRef(), p2.getRef(), p3.getRef());

            // Achats
            achatsRepo.save(Achats.builder()
                    .dateP(new Date())
                    .quantity(2.0)
                    .product(p1)
                    .user(user1)
                    .build());

            achatsRepo.save(Achats.builder()
                    .dateP(new Date())
                    .quantity(1.0)
                    .product(p2)
                    .user(user1)
                    .build());

            // Ventes
            ventesRepo.save(Ventes.builder()
                    .dateP(new Date())
                    .quantity(3.0)
                    .product(p3)
                    .user(admin)
                    .build());

            log.info("=== Données initialisées avec succès ===");
        };
    }
}
